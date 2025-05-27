package com.projet.locationvoiture.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@Builder
public class AgenceDto {

    private Long id;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private String description;
    private boolean enabled;
    private String status;
    private MultipartFile logo;
    private byte[] rlogo;
    private Long userId;
    private Set<String> roles;
}
