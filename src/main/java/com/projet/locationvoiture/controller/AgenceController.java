package com.projet.locationvoiture.controller;

import com.projet.locationvoiture.dto.AgenceDto;
import com.projet.locationvoiture.dto.CarDto;
import com.projet.locationvoiture.entity.Agence;
import com.projet.locationvoiture.entity.Car;
import com.projet.locationvoiture.services.agence.AgenceService;
import jakarta.validation.Valid;
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

    @PostMapping("/add")
    public ResponseEntity<AgenceDto> addAgence(@Valid @ModelAttribute AgenceDto dto) throws IOException {
        AgenceDto createdAgence = agenceService.addAgence(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAgence);
    }


}
