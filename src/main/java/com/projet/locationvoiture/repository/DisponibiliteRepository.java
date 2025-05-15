package com.projet.locationvoiture.repository;

import com.projet.locationvoiture.entity.Disponibilite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisponibiliteRepository extends CrudRepository<Disponibilite, Long> {
}
