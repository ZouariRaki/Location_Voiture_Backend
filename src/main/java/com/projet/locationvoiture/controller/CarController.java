package com.projet.locationvoiture.controller;

import com.projet.locationvoiture.dto.CarDto;
import com.projet.locationvoiture.services.cars.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.List;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public ResponseEntity<?> createCar(@ModelAttribute CarDto carDto) {
        boolean created = carService.createCar(carDto);
        if (created) {
            return ResponseEntity.ok("Voiture ajoutée avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout de la voiture.");
        }
    }

    @GetMapping
    public ResponseEntity<List<CarDto>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/available")
    public ResponseEntity<List<CarDto>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAvailableCars());
    }

    @GetMapping("/search/marque")
    public ResponseEntity<List<CarDto>> searchByMarque(@RequestParam String marque) {
        return ResponseEntity.ok(carService.getCarsByMarque(marque));
    }

    @GetMapping("/search/modele")
    public ResponseEntity<List<CarDto>> searchByModele(@RequestParam String modele) {
        return ResponseEntity.ok(carService.getCarsByModele(modele));
    }

    @GetMapping("/search/prix")
    public ResponseEntity<List<CarDto>> searchByPrix(@RequestParam double min, @RequestParam double max) {
        return ResponseEntity.ok(carService.getCarsByPrixRange(min, max));
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<?> deleteCar(@PathVariable Long carId) {
        boolean deleted = carService.deleteCar(carId);
        if (deleted) {
            return ResponseEntity.ok("Voiture supprimée.");
        } else {
            return ResponseEntity.badRequest().body("Voiture non trouvée.");
        }
    }
}