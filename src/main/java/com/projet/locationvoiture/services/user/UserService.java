package com.projet.locationvoiture.services.user;

import com.projet.locationvoiture.config.JwtService;
import com.projet.locationvoiture.dto.AgenceDto;
import com.projet.locationvoiture.dto.UserDto;
import com.projet.locationvoiture.entity.AccountStatus;
import com.projet.locationvoiture.entity.User;
import com.projet.locationvoiture.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;


    public UserDto getCurrentUser(String token) {
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        String email = jwtService.extractEmail(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return UserDto.builder()
                .id(user.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()))
                .build();
    }

    public List<AgenceDto> getAllAgencesUsers() {
        List<User> agencesUsers = userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().name().equals("AGENCE")))
                .collect(Collectors.toList());

        return agencesUsers.stream()
                .map(user -> {
                    var agence = user.getAgence();

                    byte[] logoBytes = null;
                    if (agence != null) {
                        logoBytes = agence.getLogo();
                    }

                    Set<String> roles = user.getRoles().stream()
                            .map(role -> role.getName().name())
                            .collect(Collectors.toSet());

                    return AgenceDto.builder()
                            .id(agence != null ? agence.getId() : null)
                            .nom(agence != null ? agence.getNom() : null)
                            .adresse(agence != null ? agence.getAdresse() : null)
                            .telephone(agence != null ? agence.getTelephone() : null)
                            .email(agence != null ? agence.getEmail() : null)
                            .description(agence != null ? agence.getDescription() : null)
                            .enabled(user.isEnabled())
                            .status(user.getStatus() != null ? user.getStatus().name() : null)
                            .rlogo(logoBytes)  // Logo image binaire

                            .userId(user.getId())
                            .roles(roles)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void updateStatus(Long id, String newStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec ID : " + id));

        AccountStatus status;
        try {
            status = AccountStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Statut invalide : " + newStatus);
        }

        user.setStatus(status);

        if (status == AccountStatus.APPROVED) {
            user.setEnabled(true);
        } else {
            user.setEnabled(false);
        }

        userRepository.save(user);
    }

}