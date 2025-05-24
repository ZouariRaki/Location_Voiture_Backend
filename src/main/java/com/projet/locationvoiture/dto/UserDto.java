package com.projet.locationvoiture.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDto {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Set<String> roles;
}
