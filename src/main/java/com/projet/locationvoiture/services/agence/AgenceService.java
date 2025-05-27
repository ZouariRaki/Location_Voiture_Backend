package com.projet.locationvoiture.services.agence;
import com.projet.locationvoiture.dto.AgenceDto;
import com.projet.locationvoiture.entity.Agence;
import com.projet.locationvoiture.repository.AgenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgenceService {

    private final AgenceRepository agenceRepository;

    public boolean addAgence(AgenceDto dto) throws IOException {
        try {
            Agence agence = new Agence();
            agence.setNom(dto.getNom());
            agence.setAdresse(dto.getAdresse());
            agence.setTelephone(dto.getTelephone());
            agence.setEmail(dto.getEmail());
            agence.setDescription(dto.getDescription());
            agence.setLogo(dto.getLogo().getBytes());
            agenceRepository.save(agence);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Agence> getAllAgences() {
        return agenceRepository.findAll();
    }

    public Optional<Agence> getAgenceById(Long id) {
        return agenceRepository.findById(id);
    }

    public boolean updateAgence(Long id, AgenceDto dto) throws IOException {
        Optional<Agence> optional = agenceRepository.findById(id);
        if (optional.isPresent()) {
            Agence agence = optional.get();
            agence.setNom(dto.getNom());
            agence.setAdresse(dto.getAdresse());
            agence.setTelephone(dto.getTelephone());
            agence.setEmail(dto.getEmail());
            agence.setDescription(dto.getDescription());
            agence.setLogo(dto.getLogo().getBytes());
            agenceRepository.save(agence);
            return true;
        }
        return false;
    }

    public boolean deleteAgence(Long id) {
        if (agenceRepository.existsById(id)) {
            agenceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

