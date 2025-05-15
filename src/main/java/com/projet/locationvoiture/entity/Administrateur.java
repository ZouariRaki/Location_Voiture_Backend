package com.projet.locationvoiture.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrateurs")
@PrimaryKeyJoinColumn(name = "user_id")
public class Administrateur extends User {

}
