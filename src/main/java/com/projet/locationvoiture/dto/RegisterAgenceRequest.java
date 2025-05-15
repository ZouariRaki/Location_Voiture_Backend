package com.projet.locationvoiture.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class RegisterAgenceRequest {

    private String nom;

    private String adresse;

    private String telephone;

    private String email;

    private String password;

    private String description;

    private MultipartFile logo;
}
