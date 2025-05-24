package com.projet.locationvoiture.services.cars;

import com.projet.locationvoiture.dto.CarDto;
import com.projet.locationvoiture.entity.Agence;
import com.projet.locationvoiture.entity.Car;
import com.projet.locationvoiture.repository.AgenceRepository;
import com.projet.locationvoiture.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final AgenceRepository agenceRepository;

    public boolean createCar(CarDto carDto) {
        try {
            Optional<Agence> agenceOpt = agenceRepository.findById(carDto.getAgenceId());
            if (agenceOpt.isEmpty()) return false;

            Car car = convertToEntity(carDto);
            car.setAgence(agenceOpt.get());

            MultipartFile image = carDto.getImage();
            if (image != null && !image.isEmpty()) {
                car.setImage(image.getBytes());
            }

            carRepository.save(car);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CarDto> getAvailableCars() {
        return carRepository.findByDisponibleTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CarDto> getCarsByMarque(String marque) {
        return carRepository.findByMarqueContainingIgnoreCase(marque).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CarDto> getCarsByModele(String modele) {
        return carRepository.findByModeleContainingIgnoreCase(modele).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CarDto> getCarsByPrixRange(double min, double max) {
        return carRepository.findByPrixJourBetween(min, max).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public boolean deleteCar(Long carId) {
        if (carRepository.existsById(carId)) {
            carRepository.deleteById(carId);
            return true;
        }
        return false;
    }

    private Car convertToEntity(CarDto dto) {
        Car car = new Car();
        car.setId(dto.getId());
        car.setMarque(dto.getMarque());
        car.setModele(dto.getModele());
        car.setAnnee(dto.getAnnee());
        car.setType(dto.getType());
        car.setCarburant(dto.getCarburant());
        car.setTransmission(dto.getTransmission());
        car.setSieges(dto.getSieges());
        car.setPortes(dto.getPortes());
        car.setClimatisation(dto.isClimatisation());
        car.setPrixJour(dto.getPrixJour());
        car.setCaution(dto.getCaution());
        car.setDisponible(dto.isDisponible());
        return car;
    }

    private CarDto convertToDto(Car car) {
        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setMarque(car.getMarque());
        dto.setModele(car.getModele());
        dto.setAnnee(car.getAnnee());
        dto.setType(car.getType());
        dto.setCarburant(car.getCarburant());
        dto.setTransmission(car.getTransmission());
        dto.setSieges(car.getSieges());
        dto.setPortes(car.getPortes());
        dto.setClimatisation(car.isClimatisation());
        dto.setPrixJour(car.getPrixJour());
        dto.setCaution(car.getCaution());
        dto.setAgenceId(car.getAgence() != null ? car.getAgence().getId() : null);
        dto.setDisponible(car.getDisponible());
        dto.setRimage(car.getImage());
        return dto;
    }
}
