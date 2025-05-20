package com.projet.locationvoiture.services.client;

import com.projet.locationvoiture.entity.Client;
import com.projet.locationvoiture.entity.Reservation;
import com.projet.locationvoiture.repository.ClientRepository;
import com.projet.locationvoiture.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
@Autowired
private ReservationRepository reservationRepository;
    public Client ajouterClient(Client client) {
        return clientRepository.save(client);
    }
    public List<Reservation> consulterHistorique(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }
}
