package com.projet.locationvoiture.controller;

import com.projet.locationvoiture.dto.AgenceDto;
import com.projet.locationvoiture.dto.UserDto;
import com.projet.locationvoiture.entity.User;
import com.projet.locationvoiture.services.AuthService;
import com.projet.locationvoiture.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private  AuthService authService;
@Autowired
private UserService userService;
    @PostMapping("/valider-agence/{id}")
    public ResponseEntity<String> validerAgence(@PathVariable Long id) {
        try {
            authService.validateAgence(id);
            return ResponseEntity.ok("Agence validée et email envoyé.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/ag")
    public ResponseEntity<List<AgenceDto>> getAllAgencesUsers() {
        List<AgenceDto> agences = userService.getAllAgencesUsers();
        return ResponseEntity.ok(agences);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            userService.updateStatus(id, status);
            return ResponseEntity.ok("Statut mis à jour avec succès.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }


}
