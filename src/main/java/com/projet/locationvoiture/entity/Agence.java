package com.projet.locationvoiture.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "agences")
@Data
public class Agence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    @Column(columnDefinition = "longblob")
    private byte[] logo;
    private String description;
    @OneToMany(mappedBy = "agence")
    private List<Car> vehicules;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
