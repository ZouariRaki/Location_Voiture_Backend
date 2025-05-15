package com.projet.locationvoiture.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AgenceDto {

    private Long id;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private String description;
    private MultipartFile logo;
    private byte[] multipartFile;
}
