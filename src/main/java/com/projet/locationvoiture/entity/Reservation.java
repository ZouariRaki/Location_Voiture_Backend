package com.projet.locationvoiture.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "reservations")
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Double montant;
    @Column(updatable = false)
    private LocalDateTime dateCreation; // Pour audit

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }
    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Car vehicule;

    @ManyToOne
    private Agence agence;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Paiement paiement;
}