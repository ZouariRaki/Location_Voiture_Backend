package com.projet.locationvoiture.dto;

import com.projet.locationvoiture.entity.AccountStatus;
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
    private String telephone;
    private boolean enabled;
    private AccountStatus status;
    private Set<String> roles;
}
