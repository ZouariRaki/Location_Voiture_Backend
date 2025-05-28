package com.projet.locationvoiture.controller;

import com.projet.locationvoiture.config.JwtService;
import com.projet.locationvoiture.dto.*;
import com.projet.locationvoiture.entity.ERole;
import com.projet.locationvoiture.entity.User;
import com.projet.locationvoiture.services.AuthService;
import com.projet.locationvoiture.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegisterRequest request) {
        RegistrationResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register-agence")
    public ResponseEntity<String> registerAgence(
            @RequestPart("nom") String nom,
            @RequestPart("adresse") String adresse,
            @RequestPart("telephone") String telephone,
            @RequestPart("email") String email,
            @RequestPart("password") String password,
            @RequestPart("description") String description,
            @RequestPart(value = "logo", required = false) MultipartFile logo) {

        RegisterAgenceRequest request = new RegisterAgenceRequest();
        request.setNom(nom);
        request.setAdresse(adresse);
        request.setTelephone(telephone);
        request.setEmail(email);
        request.setPassword(password);
        request.setDescription(description);
        request.setLogo(logo);

        authService.registerAgence(request);
        return ResponseEntity.ok("Agence enregistrée avec succès. Vérifiez votre email.");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        authService.verifyUser(token);
        return ResponseEntity.ok("Compte vérifié avec succès !");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody PasswordResetRequest request) {
        authService.requestPasswordReset(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody NewPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        UserDto userDto = userService.getCurrentUser(authHeader);
        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/admin/add-agence")
    public ResponseEntity<String> adminAddAgence(
            @RequestHeader("Authorization") String authHeader,
            @RequestPart("nom") String nom,
            @RequestPart("adresse") String adresse,
            @RequestPart("telephone") String telephone,
            @RequestPart("email") String email,
            @RequestPart("description") String description,
            @RequestPart(value = "logo", required = false) MultipartFile logo) {

        // Vérifier que l'utilisateur est un administrateur
        String jwt = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        String emailAdmin = jwtService.extractEmail(jwt);
        User admin = authService.getUserByEmail(emailAdmin);
        boolean isAdmin = admin.getRoles().stream()
                .anyMatch(role -> role.getName().equals(ERole.ADMINISTRATEUR));
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Seul un administrateur peut ajouter une agence");
        }

        AgenceDto request = AgenceDto.builder()
                .nom(nom)
                .adresse(adresse)
                .telephone(telephone)
                .email(email)
                .description(description)
                .logo(logo)
                .build();

        authService.adminAddAgence(request);
        return ResponseEntity.ok("Agence ajoutée avec succès");
    }
    @PutMapping("/agences/{id}")
    public ResponseEntity<String> adminUpdateAgence(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") Long agenceId,
            @RequestPart(value = "nom", required = false) String nom,
            @RequestPart(value = "adresse", required = false) String adresse,
            @RequestPart(value = "telephone", required = false) String telephone,
            @RequestPart(value = "email", required = false) String email,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "logo", required = false) MultipartFile logo) {

        // Verify admin role
        String jwt = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        String emailAdmin = jwtService.extractEmail(jwt);
        User admin = authService.getUserByEmail(emailAdmin);
        boolean isAdmin = admin.getRoles().stream()
                .anyMatch(role -> role.getName().equals(ERole.ADMINISTRATEUR));
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Seul un administrateur peut mettre à jour une agence");
        }

        AgenceDto request = AgenceDto.builder()
                .nom(nom)
                .adresse(adresse)
                .telephone(telephone)
                .email(email)
                .description(description)
                .logo(logo)
                .build();

        authService.adminUpdateAgence(agenceId, request);
        return ResponseEntity.ok("Agence mise à jour avec succès");
    }

    @DeleteMapping("/agences/{id}")
    public ResponseEntity<String> adminDeleteAgence(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("id") Long agenceId) {
        try {
            // Validate Authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("En-tête d'autorisation manquant ou invalide");
            }

            // Extract JWT and email
            String jwt = authHeader.substring(7);
            String emailAdmin;
            try {
                emailAdmin = jwtService.extractEmail(jwt);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Jeton JWT invalide");
            }

            // Verify admin user
            User admin = authService.getUserByEmail(emailAdmin);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Utilisateur non trouvé");
            }

            // Verify admin role
            boolean isAdmin = admin.getRoles().stream()
                    .anyMatch(role -> role.getName().equals(ERole.ADMINISTRATEUR));
            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Seul un administrateur peut supprimer une agence");
            }

            // Delete agency
            authService.adminDeleteAgence(agenceId);
            return ResponseEntity.ok("Agence supprimée avec succès");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de la suppression de l'agence: " + e.getMessage());
        }
    }
}