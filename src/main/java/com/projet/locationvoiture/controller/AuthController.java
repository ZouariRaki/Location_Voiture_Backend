package com.projet.locationvoiture.controller;

import com.projet.locationvoiture.dto.*;
import com.projet.locationvoiture.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
}
