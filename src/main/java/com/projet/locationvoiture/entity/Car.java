package com.projet.locationvoiture.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marque;
    private String modele;
    private int annee;
    private String type;
    private String carburant;
    private String transmission;
    private int sieges;
    private int portes;
    private boolean climatisation;
    private double prixJour;
    @Column(columnDefinition = "longblob")
    @JsonIgnore
    private byte[] image;
    @PositiveOrZero
    private double caution;
    @NotNull
    private Boolean disponible;
    @Column(nullable = false)
    private String statut = "AVAILABLE";
    @ManyToOne
    @JoinColumn(name = "agence_id")
    @JsonIgnore
    private Agence agence;



    @OneToMany(mappedBy = "vehicule")
    private List<Reservation> reservations;


}
