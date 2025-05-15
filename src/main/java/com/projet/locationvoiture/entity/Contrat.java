package com.projet.locationvoiture.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fichierPdf;
    private String etatInitial;
    private String etatFinal;

    @OneToOne
    private Reservation reservation;
}

