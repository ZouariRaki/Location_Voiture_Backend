package com.projet.locationvoiture.services.agence;
import com.projet.locationvoiture.dto.AgenceDto;
import com.projet.locationvoiture.entity.Agence;
import com.projet.locationvoiture.entity.User;
import com.projet.locationvoiture.repository.AgenceRepository;
import com.projet.locationvoiture.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgenceService {

    private final AgenceRepository agenceRepository;
    private final UserRepository userRepository;

    public AgenceDto addAgence(@Valid AgenceDto dto) throws IOException {
        // Vérifier si l'email est déjà utilisé
        if (agenceRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("L'email est déjà utilisé par une autre agence");
        }

        Agence agence = new Agence();
        agence.setNom(dto.getNom());
        agence.setAdresse(dto.getAdresse());
        agence.setTelephone(dto.getTelephone());
        agence.setEmail(dto.getEmail());
        agence.setDescription(dto.getDescription());

        // Gérer le logo
        if (dto.getLogo() != null && !dto.getLogo().isEmpty()) {
            agence.setLogo(dto.getLogo().getBytes());
        }

        // Associer un utilisateur si userId est fourni
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + dto.getUserId()));
            agence.setUser(user);
        }

        agence = agenceRepository.save(agence);

        // Retourner l'AgenceDto avec les données enregistrées
        return AgenceDto.builder()
                .id(agence.getId())
                .nom(agence.getNom())
                .adresse(agence.getAdresse())
                .telephone(agence.getTelephone())
                .email(agence.getEmail())
                .description(agence.getDescription())
                .rlogo(agence.getLogo())
                .userId(agence.getUser() != null ? agence.getUser().getId() : null)
                .build();
    }

    public List<Agence> getAllAgences() {
        return agenceRepository.findAll();
    }

    public Optional<Agence> getAgenceById(Long id) {
        return agenceRepository.findById(id);
    }

    public boolean updateAgence(Long id, AgenceDto dto) throws IOException {
        Optional<Agence> optional = agenceRepository.findById(id);
        if (optional.isPresent()) {
            Agence agence = optional.get();
            agence.setNom(dto.getNom());
            agence.setAdresse(dto.getAdresse());
            agence.setTelephone(dto.getTelephone());
            agence.setEmail(dto.getEmail());
            agence.setDescription(dto.getDescription());
            agence.setLogo(dto.getLogo().getBytes());
            agenceRepository.save(agence);
            return true;
        }
        return false;
    }

    public boolean deleteAgence(Long id) {
        if (agenceRepository.existsById(id)) {
            agenceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

