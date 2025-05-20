package com.projet.locationvoiture.repository;

import com.projet.locationvoiture.entity.AccountStatus;
import com.projet.locationvoiture.entity.ERole;
import com.projet.locationvoiture.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Méthodes existantes
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String verificationToken);

    // Pour le mot de passe oublié
    Optional<User> findByResetToken(String resetToken);

    // Pour la vérification d'existence
    boolean existsByEmail(String email);

    // Pour la gestion des rôles
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1")
    List<User> findByRole(ERole role);

    // Pour l'admin : utilisateurs en attente de validation
    List<User> findByStatus(AccountStatus status);

    // Mises à jour spécifiques
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = ?2 WHERE u.id = ?1")
    void updatePassword(Long userId, String newPassword);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.resetToken = ?2, u.resetTokenExpiry = ?3 WHERE u.id = ?1")
    void updateResetToken(Long userId, String token, Date expiry);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.enabled = true, u.verificationToken = null WHERE u.id = ?1")
    void enableUser(Long userId);
}