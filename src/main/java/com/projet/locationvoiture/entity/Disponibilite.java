package com.projet.locationvoiture.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "disponibilites")
@Data
public class Disponibilite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDisponibilite statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    public boolean chevauche(LocalDate debut, LocalDate fin) {
        return !dateFin.isBefore(debut) && !dateDebut.isAfter(fin);
    }

    public enum StatutDisponibilite {
        DISPONIBLE,
        RESERVE,
        MAINTENANCE,
        HORS_SERVICE
    }
}
