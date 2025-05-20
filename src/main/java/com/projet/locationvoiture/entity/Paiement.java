package com.projet.locationvoiture.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "paiements")
@Data
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double montant;
    private LocalDateTime date;
    private String moyenPaiement;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut;

    @OneToOne
    @JoinColumn(name = "reservation_id") // Meilleure pratique pour le owner de la relation
    private Reservation reservation;
}
