package com.projet.locationvoiture.services.reservation;

import com.projet.locationvoiture.dto.ReservationDto;
import com.projet.locationvoiture.entity.Car;
import com.projet.locationvoiture.entity.Reservation;
import com.projet.locationvoiture.entity.StatutReservation;
import com.projet.locationvoiture.entity.User;
import com.projet.locationvoiture.repository.CarRepository;
import com.projet.locationvoiture.repository.ReservationRepository;
import com.projet.locationvoiture.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public boolean createReservation(ReservationDto reservationDto) {
        try {
            Optional<Car> carOpt = carRepository.findById(reservationDto.getCarId());
            Optional<User> userOpt = userRepository.findById(reservationDto.getUserId());

            if (carOpt.isEmpty() || userOpt.isEmpty()) {
                return false;
            }

            Car car = carOpt.get();
            User user = userOpt.get();

            Reservation reservation = new Reservation();
            reservation.setVehicule(car); // Assurez-vous que "vehicule" est bien le bon nom
            reservation.setClient(user);  // Assurez-vous que "client" est bien le bon nom
            reservation.setDateDebut(reservationDto.getDateDebut());
            reservation.setDateFin(reservationDto.getDateFin());
            reservation.setStatut(StatutReservation.EN_ATTENTE);
            reservation.setMontant(calculateTotal(reservationDto.getDateDebut(), reservationDto.getDateFin(), car.getPrixJour()));

            reservationRepository.save(reservation);
            return true;

        } catch (Exception e) {
            // log.error("Erreur lors de la création de la réservation", e);
            return false;
        }
    }

    /**
     * Retourne les réservations d’un utilisateur.
     */
    public List<ReservationDto> getReservationsByUser(Long userId) {
        return reservationRepository.findByClientId(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retourne les réservations pour une agence spécifique.
     */
    public List<ReservationDto> getReservationsByAgency(Long agencyId) {
        return reservationRepository.findByVehiculeAgenceId(agencyId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour le statut d'une réservation.
     */
    public boolean updateReservationStatus(Long reservationId, String status) {
        try {
            StatutReservation statutEnum = StatutReservation.valueOf(status.toUpperCase());
            return reservationRepository.findById(reservationId)
                    .map(reservation -> {
                        reservation.setStatut(statutEnum);
                        reservationRepository.save(reservation);
                        return true;
                    }).orElse(false);
        } catch (IllegalArgumentException e) {
            // log.warn("Statut invalide fourni : {}", status);
            return false;
        }
    }

    /**
     * Annule une réservation.
     */
    public boolean cancelReservation(Long reservationId) {
        return updateReservationStatus(reservationId, "ANNULEE");
    }

    /**
     * Supprime une réservation par son ID.
     */
    public boolean deleteReservation(Long reservationId) {
        if (reservationRepository.existsById(reservationId)) {
            reservationRepository.deleteById(reservationId);
            return true;
        }
        return false;
    }

    /**
     * Récupère toutes les réservations existantes.
     */
    public List<ReservationDto> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une entité Reservation en DTO.
     */
    private ReservationDto convertToDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .userId(reservation.getClient().getId())
                .carId(reservation.getVehicule().getId())
                .dateDebut(reservation.getDateDebut())
                .dateFin(reservation.getDateFin())
                .statut(reservation.getStatut())
                .montant(reservation.getMontant())
                .build();
    }

    /**
     * Calcule le montant total de la réservation.
     */
    private double calculateTotal(LocalDateTime dateDebut, LocalDateTime dateFin, double prixParJour) {
        long jours = ChronoUnit.DAYS.between(dateDebut, dateFin);
        if (jours <= 0) jours = 1; // Minimum 1 jour
        return jours * prixParJour;
    }
}
