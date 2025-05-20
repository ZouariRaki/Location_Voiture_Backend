package com.projet.locationvoiture.services.paiement;

import com.projet.locationvoiture.entity.Paiement;
import com.projet.locationvoiture.entity.StatutPaiement;
import com.projet.locationvoiture.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaiementService {
    @Autowired
    private PaiementRepository paiementRepository;


    public Paiement traiterPaiement(Paiement paiement) {
        paiement.setStatut(StatutPaiement.EFFECTUE);
        return paiementRepository.save(paiement);
    }


    public Paiement rembourser(Long id) {
        Paiement p = paiementRepository.findById(id).orElseThrow();
        p.setStatut(StatutPaiement.REMBOURSE);
        return paiementRepository.save(p);
    }
}
