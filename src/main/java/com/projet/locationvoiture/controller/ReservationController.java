package com.projet.locationvoiture.controller;

import com.projet.locationvoiture.dto.ReservationDto;
import com.projet.locationvoiture.services.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;


    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationDto reservationDto) {
        boolean created = reservationService.createReservation(reservationDto);
        if (created) {
            return ResponseEntity.ok("Réservation créée avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Impossible de créer la réservation.");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDto>> getReservationsByUser(@PathVariable Long userId) {
        List<ReservationDto> reservations = reservationService.getReservationsByUser(userId);
        return ResponseEntity.ok(reservations);
    }


    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<List<ReservationDto>> getReservationsByAgency(@PathVariable Long agencyId) {
        List<ReservationDto> reservations = reservationService.getReservationsByAgency(agencyId);
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{reservationId}/status")
    public ResponseEntity<?> updateReservationStatus(
            @PathVariable Long reservationId,
            @RequestParam String status) {
        boolean updated = reservationService.updateReservationStatus(reservationId, status);
        if (updated) {
            return ResponseEntity.ok("Statut de la réservation mis à jour.");
        } else {
            return ResponseEntity.badRequest().body("Statut invalide ou réservation non trouvée.");
        }
    }

    @PutMapping("/{reservationId}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        boolean cancelled = reservationService.cancelReservation(reservationId);
        if (cancelled) {
            return ResponseEntity.ok("Réservation annulée.");
        } else {
            return ResponseEntity.badRequest().body("Réservation non trouvée ou erreur lors de l'annulation.");
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId) {
        boolean deleted = reservationService.deleteReservation(reservationId);
        if (deleted) {
            return ResponseEntity.ok("Réservation supprimée.");
        } else {
            return ResponseEntity.badRequest().body("Réservation non trouvée.");
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
}
