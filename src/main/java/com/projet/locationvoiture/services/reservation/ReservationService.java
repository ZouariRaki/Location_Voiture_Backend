package com.projet.locationvoiture.services.reservation;

import com.projet.locationvoiture.entity.Reservation;
import com.projet.locationvoiture.entity.StatutReservation;
import com.projet.locationvoiture.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;


    public Reservation reserver(Reservation reservation) {
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        return reservationRepository.save(reservation);
    }


    public Reservation confirmer(Long id) {
        Reservation r = reservationRepository.findById(id).orElseThrow();
        r.setStatut(StatutReservation.CONFIRMEE);
        return reservationRepository.save(r);
    }


    public Reservation annuler(Long id) {
        Reservation r = reservationRepository.findById(id).orElseThrow();
        r.setStatut(StatutReservation.ANNULEE);
        return reservationRepository.save(r);
    }
}
