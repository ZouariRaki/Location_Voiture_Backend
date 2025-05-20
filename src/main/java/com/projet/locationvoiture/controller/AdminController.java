package com.projet.locationvoiture.controller;

import com.projet.locationvoiture.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private  AuthService authService;

    @PostMapping("/valider-agence/{id}")
    public ResponseEntity<String> validerAgence(@PathVariable Long id) {
        try {
            authService.validateAgence(id);
            return ResponseEntity.ok("Agence validée et email envoyé.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

}
