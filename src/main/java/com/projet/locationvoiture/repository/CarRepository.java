package com.projet.locationvoiture.repository;

import com.projet.locationvoiture.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByDisponibleTrue(); // voitures disponibles

    List<Car> findByAgenceId(Long agenceId); // voitures d'une agence

    List<Car> findByMarqueContainingIgnoreCase(String marque); // recherche par marque

    List<Car> findByModeleContainingIgnoreCase(String modele); // recherche par modèle

    List<Car> findByPrixJourBetween(double min, double max); // filtre par prix

}
