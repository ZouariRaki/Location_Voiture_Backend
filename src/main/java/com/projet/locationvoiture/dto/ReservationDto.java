package com.projet.locationvoiture.dto;

import com.projet.locationvoiture.entity.StatutReservation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDto {
    private Long id;
    private Long userId;
    private Long carId;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private StatutReservation statut;
    private Double montant;
}
