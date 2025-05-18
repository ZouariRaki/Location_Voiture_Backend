package com.projet.locationvoiture.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    private double caution;
    private boolean disponible;
    @Column(columnDefinition = "longblob")
    @JsonIgnore
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "agence_id")
    @JsonIgnore
    private Agence agence;

    @OneToMany(mappedBy = "car")
    private List<Disponibilite> disponibilites;

    @OneToMany(mappedBy = "car")
    private List<Reservation> reservations;


}
