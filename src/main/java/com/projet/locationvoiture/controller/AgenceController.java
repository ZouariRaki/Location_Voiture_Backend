package com.projet.locationvoiture.controller;

import com.projet.locationvoiture.dto.AgenceDto;
import com.projet.locationvoiture.dto.CarDto;
import com.projet.locationvoiture.entity.Agence;
import com.projet.locationvoiture.entity.Car;
import com.projet.locationvoiture.services.agence.AgenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final AgenceService agenceService;

    @PostMapping("add")
    public ResponseEntity<?> addAgence(@ModelAttribute AgenceDto dto) throws IOException {
        boolean success = agenceService.addAgence(dto);
        return success ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("all")
    public ResponseEntity<List<Agence>> getAll() {
        return ResponseEntity.ok(agenceService.getAllAgences());
    }

    @GetMapping("{id}")
    public ResponseEntity<Agence> getById(@PathVariable Long id) {
        Optional<Agence> agence = agenceService.getAgenceById(id);
        return agence.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @ModelAttribute AgenceDto dto) throws IOException {
        boolean success = agenceService.updateAgence(id, dto);
        return success ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean success = agenceService.deleteAgence(id);
        return success ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("/{agenceId}/cars")
    public ResponseEntity<?> addCarToAgence(
            @PathVariable Long agenceId,
            @ModelAttribute CarDto carDto) throws IOException {
        boolean success = agenceService.addCarToAgence(carDto, agenceId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


}
