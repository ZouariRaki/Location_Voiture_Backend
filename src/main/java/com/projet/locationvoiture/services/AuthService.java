package com.projet.locationvoiture.services;
import com.projet.locationvoiture.config.JwtService;
import com.projet.locationvoiture.dto.*;
import com.projet.locationvoiture.entity.*;
import com.projet.locationvoiture.exception.EmailAlreadyExistsException;
import com.projet.locationvoiture.mail.EmailService;
import com.projet.locationvoiture.repository.AgenceRepository;
import com.projet.locationvoiture.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final long PASSWORD_RESET_EXPIRATION = 3600000;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AgenceRepository agenceRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @PostConstruct
    public void createAdminAccount() {
        boolean adminExists = userRepository.findAll().stream()
                .anyMatch(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().name().equals("ADMINISTRATEUR")));
        if (!adminExists) {
            User newAdminAccount = new User();
            newAdminAccount.setNom("Admin");
            newAdminAccount.setPrenom("Admin");
            newAdminAccount.setEmail("raki@gmail.com");
            newAdminAccount.setRoles(Set.of(new Role(ERole.ADMINISTRATEUR)));
            newAdminAccount.setPassword(passwordEncoder.encode("admin"));
            newAdminAccount.setEnabled(true);
            userRepository.save(newAdminAccount);
            System.out.println("Admin account created");
        }
    }

    public RegistrationResponse register(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsException("L'email " + request.getEmail() + " est déjà utilisé");
                });

        User user = User.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(new Role(ERole.CLIENT)))
                .enabled(false)
                .verificationToken(UUID.randomUUID().toString())
                .build();

        userRepository.save(user);

        try {
            emailService.sendVerificationEmail(user);
        } catch (Exception e) {
            userRepository.delete(user);
            throw new RuntimeException("Échec de l'envoi de l'email de vérification");
        }

        return RegistrationResponse.builder()
                .message("Email de vérification envoyé")
                .build();
    }

    public void registerAgence(RegisterAgenceRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsException("L'email " + request.getEmail() + " est déjà utilisé");
                });

        User user = new User();
        user.setNom(request.getNom());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(new Role(ERole.AGENCE)));
        user.setEmail(request.getEmail());
        user.setAdresse(request.getAdresse());
        user.setTelephone(request.getTelephone());
        user.setEnabled(false);
        user.setVerificationToken(UUID.randomUUID().toString());

        userRepository.save(user);

        Agence agence = new Agence();
        agence.setNom(request.getNom());
        agence.setAdresse(request.getAdresse());
        agence.setEmail(request.getEmail());
        agence.setTelephone(request.getTelephone());
        agence.setDescription(request.getDescription());

        // Exemple de traitement du logo
        try {
            MultipartFile logoFile = request.getLogo();
            if (logoFile != null && !logoFile.isEmpty()) {
                byte[] logoBytes = logoFile.getBytes();
                agence.setLogo(logoBytes); // ou enregistrer sur disque + path
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du traitement du logo", e);
        }

        // Lier user à agence si besoin
        agence.setUser(user);

        agenceRepository.save(agence); // <-- IMPORTANT

        String subject = "Bienvenue sur notre plateforme";
        String msj = "Merci de vous être inscrit en tant qu'agence. Veuillez vérifier votre compte.";
        emailService.sendSimpleMail(user.getEmail(), subject, msj);
    }
    public void validateAgence(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Vérifie que c'est bien une agence
        boolean isAgence = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(ERole.AGENCE));
        if (!isAgence) {
            throw new RuntimeException("Cet utilisateur n'est pas une agence");
        }

        // Active le compte
        user.setEnabled(true);
        user.setStatus(AccountStatus.APPROVED);
        // Réinitialise le mot de passe en utilisant le numéro de téléphone
        String tempPassword = user.getTelephone();
        user.setPassword(passwordEncoder.encode(tempPassword));

        userRepository.save(user);

        // Prépare l'email
        String subject = "Votre compte agence a été activé";
        String message = String.format(
                "Bonjour %s,\n\nVotre compte agence a été validé avec succès.\n\n" +
                        "Vous pouvez maintenant vous connecter avec les identifiants suivants :\n" +
                        "- Email : %s\n" +
                        "- Mot de passe  : %s\n\n" +
                        "Veuillez modifier votre mot de passe après connexion.\n\nCordialement,\nL'équipe Location Voiture",
                user.getNom(), user.getEmail(), tempPassword
        );

        // Envoi de l'email
        emailService.sendSimpleMail(user.getEmail(), subject, message);
    }



    public void verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Token de vérification invalide"));

        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("Compte non vérifié. Veuillez vérifier votre email.");
        }

        // Récupérer le premier rôle pour l'inclure dans la réponse
        String firstRole = user.getRoles().stream()
                .findFirst()
                .map(r -> r.getName().name())
                .orElse("NO_ROLE");

        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .email(user.getEmail())
                .role(firstRole)
                .id(user.getId())
                .build();
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void requestPasswordReset(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Aucun compte associé à cet email"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(new Date(System.currentTimeMillis() + PASSWORD_RESET_EXPIRATION));
        userRepository.save(user);

        try {
            emailService.sendPasswordResetEmail(user, resetToken);
        } catch (Exception e) {
            throw new RuntimeException("Échec de l'envoi de l'email de réinitialisation");
        }
    }

    public void resetPassword(NewPasswordRequest request) {
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token invalide ou expiré"));

        if (user.getResetTokenExpiry().before(new Date())) {
            throw new RuntimeException("Token expiré");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    public void adminAddAgence(AgenceDto request) {
        // Vérifier si l'email existe déjà
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsException("L'email " + request.getEmail() + " est déjà utilisé");
                });

        // Créer un nouvel utilisateur avec le rôle AGENCE
        User user = User.builder()
                .nom(request.getNom())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getTelephone())) // Utiliser le téléphone comme mot de passe initial
                .roles(Set.of(new Role(ERole.AGENCE)))
                .adresse(request.getAdresse())
                .telephone(request.getTelephone())
                .enabled(true) // Activer immédiatement
                .status(AccountStatus.APPROVED)
                .build();

        userRepository.save(user);

        // Créer l'agence associée
        Agence agence = new Agence();
        agence.setNom(request.getNom());
        agence.setAdresse(request.getAdresse());
        agence.setEmail(request.getEmail());
        agence.setTelephone(request.getTelephone());
        agence.setDescription(request.getDescription());
        agence.setUser(user);

        // Traiter le logo
        try {
            MultipartFile logoFile = request.getLogo();
            if (logoFile != null && !logoFile.isEmpty()) {
                byte[] logoBytes = logoFile.getBytes();
                agence.setLogo(logoBytes);
            }
        } catch (IOException e) {
            userRepository.delete(user); // Rollback en cas d'erreur
            throw new RuntimeException("Erreur lors du traitement du logo", e);
        }

        agenceRepository.save(agence);

        // Envoyer un email de bienvenue avec les informations de connexion
        String subject = "Création de votre compte agence";
        String message = String.format(
                "Bonjour %s,\n\nVotre compte agence a été créé avec succès.\n\n" +
                        "Vos identifiants de connexion sont :\n" +
                        "- Email : %s\n" +
                        "- Mot de passe temporaire : %s\n\n" +
                        "Veuillez modifier votre mot de passe après votre première connexion.\n\nCordialement,\nL'équipe Location Voiture",
                user.getNom(), user.getEmail(), user.getTelephone()
        );

        emailService.sendSimpleMail(user.getEmail(), subject, message);
    }
    public void adminUpdateAgence(Long agenceId, AgenceDto request) {
        // Find the agency and associated user
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new RuntimeException("Agence non trouvée avec l'ID: " + agenceId));
        User user = agence.getUser();
        if (user == null) {
            throw new RuntimeException("Utilisateur associé à l'agence non trouvé");
        }

        // Check if the new email is already used by another user
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            userRepository.findByEmail(request.getEmail())
                    .ifPresent(u -> {
                        throw new EmailAlreadyExistsException("L'email " + request.getEmail() + " est déjà utilisé");
                    });
        }

        // Update user fields if provided
        if (request.getNom() != null) {
            user.setNom(request.getNom());
            agence.setNom(request.getNom());
        }
        if (request.getAdresse() != null) {
            user.setAdresse(request.getAdresse());
            agence.setAdresse(request.getAdresse());
        }
        if (request.getTelephone() != null) {
            user.setTelephone(request.getTelephone());
            agence.setTelephone(request.getTelephone());
            // Update password to match new telephone (as per your add logic)
            user.setPassword(passwordEncoder.encode(request.getTelephone()));
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
            agence.setEmail(request.getEmail());
        }
        if (request.getDescription() != null) {
            agence.setDescription(request.getDescription());
        }

        // Handle logo update
        if (request.getLogo() != null && !request.getLogo().isEmpty()) {
            try {
                byte[] logoBytes = request.getLogo().getBytes();
                agence.setLogo(logoBytes);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors du traitement du logo", e);
            }
        }

        // Save updated entities
        userRepository.save(user);
        agenceRepository.save(agence);

        // Send email notification about the update
        String subject = "Mise à jour de votre compte agence";
        String message = String.format(
                "Bonjour %s,\n\nVotre compte agence a été mis à jour.\n\n" +
                        "Nouveaux détails :\n" +
                        "- Nom : %s\n" +
                        "- Email : %s\n" +
                        "- Téléphone : %s\n\n" +
                        "Si vous avez modifié votre téléphone, votre mot de passe temporaire est maintenant votre nouveau numéro de téléphone.\n" +
                        "Veuillez modifier votre mot de passe après votre prochaine connexion.\n\nCordialement,\nL'équipe Location Voiture",
                user.getNom(), user.getNom(), user.getEmail(), user.getTelephone()
        );
        emailService.sendSimpleMail(user.getEmail(), subject, message);
    }

    @Transactional
    public void adminDeleteAgence(Long agenceId) {
        // Find the agency
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new RuntimeException("Agence non trouvée avec l'ID: " + agenceId));
        User user = agence.getUser();
        if (user == null) {
            throw new RuntimeException("Utilisateur associé à l'agence non trouvé");
        }

        // Clear bidirectional relationship (if exists)
        if (user.getAgence() != null) {
            user.setAgence(null); // Assuming User has a field @OneToOne private Agence agence
            userRepository.save(user); // Persist the change
        }

        // Delete agency and flush to ensure it's removed
        agenceRepository.delete(agence);
        entityManager.flush();

        // Delete user
        userRepository.delete(user);

        // Send email notification
        String subject = "Suppression de votre compte agence";
        String message = String.format(
                "Bonjour %s,\n\nVotre compte agence a été supprimé.\n\n" +
                        "Si cela n'était pas intentionnel, veuillez contacter notre support.\n\nCordialement,\nL'équipe Location Voiture",
                user.getNom()
        );
        emailService.sendSimpleMail(user.getEmail(), subject, message);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }


}
