package com.projet.locationvoiture.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@Builder
public class AgenceDto {
    private Long id;

    @NotBlank(message = "Le nom est requis")
    private String nom;

    @NotBlank(message = "L'adresse est requise")
    private String adresse;


    private String telephone;

    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "La description est requise")
    private String description;
    private boolean enabled;
    private String status;
    private MultipartFile logo;
    private byte[] rlogo;
    private Long userId;
    private Set<String> roles;
}
