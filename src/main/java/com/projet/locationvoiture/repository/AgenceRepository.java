package com.projet.locationvoiture.repository;

import com.projet.locationvoiture.entity.Agence;
import com.projet.locationvoiture.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgenceRepository extends JpaRepository<Agence, Long> {
    Optional<Agence> findByUserId(Long userId);
    Optional<Agence> findByEmail(String email);
}
