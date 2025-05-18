package com.projet.locationvoiture.repository;

import com.projet.locationvoiture.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c WHERE c.agence.user.id = :userId")
    List<Car> findAllByUserId(@Param("userId") Long userId);

    List<Car> findByAgenceUserId(Long userId);

}
