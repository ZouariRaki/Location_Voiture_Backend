package com.projet.locationvoiture.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CarDto {
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
    private boolean disponible;
    private double prixJour;
    private double caution;
    private byte[] rimage;
    private MultipartFile image;
    private Long agenceId;

}
