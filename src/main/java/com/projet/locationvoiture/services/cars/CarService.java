package com.projet.locationvoiture.services.cars;

import com.projet.locationvoiture.dto.CarDto;
import com.projet.locationvoiture.entity.Agence;
import com.projet.locationvoiture.entity.Car;
import com.projet.locationvoiture.repository.AgenceRepository;
import com.projet.locationvoiture.repository.CarRepository;
import com.projet.locationvoiture.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;
@Autowired
private UserRepository userRepository;
@Autowired
private AgenceRepository agenceRepository;
    public boolean addCar(CarDto carDto) throws IOException {
        try {
            Car car = new Car();
            car.setMarque(carDto.getMarque());
            car.setModele(carDto.getModele());
            car.setAnnee(carDto.getAnnee());
            car.setType(carDto.getType());
            car.setCarburant(carDto.getCarburant());
            car.setTransmission(carDto.getTransmission());
            car.setSieges(carDto.getSieges());
            car.setPortes(carDto.getPortes());
            car.setClimatisation(carDto.isClimatisation());
            car.setPrixJour(carDto.getPrixJour());
            car.setCaution(carDto.getCaution());
            car.setImage(carDto.getImage().getBytes());
            carRepository.save(car);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Dans CarService
    public List<CarDto> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(this::convertToDto).toList();
    }

    private CarDto convertToDto(Car cars) {
        CarDto carDto = new CarDto();
        carDto.setId(cars.getId());
        carDto.setMarque(cars.getMarque());
        carDto.setModele(cars.getModele());
        carDto.setAnnee(cars.getAnnee());
        carDto.setType(cars.getType());
        carDto.setCarburant(cars.getCarburant());
        carDto.setTransmission(cars.getTransmission());
        carDto.setSieges(cars.getSieges());
        carDto.setPortes(cars.getPortes());
        carDto.setClimatisation(cars.isClimatisation());
        carDto.setPrixJour(cars.getPrixJour());
        carDto.setCaution(cars.getCaution());
        carDto.setRimage(cars.getImage()); // L'image est déjà en byte[]
        return carDto;
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public boolean updateCar(Long id, CarDto carDto) throws IOException {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            car.setMarque(carDto.getMarque());
            car.setModele(carDto.getModele());
            car.setAnnee(carDto.getAnnee());
            car.setType(carDto.getType());
            car.setCarburant(carDto.getCarburant());
            car.setTransmission(carDto.getTransmission());
            car.setSieges(carDto.getSieges());
            car.setPortes(carDto.getPortes());
            car.setClimatisation(carDto.isClimatisation());
            car.setPrixJour(carDto.getPrixJour());
            car.setCaution(carDto.getCaution());
            car.setImage(carDto.getImage().getBytes());
            carRepository.save(car);
            return true;
        }
        return false;
    }

    public boolean deleteCar(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }

public boolean addCarForUserId(CarDto carDto, Long userId) throws IOException {
    try {
        // Récupérer l'agence liée à l'utilisateur
        Agence agence = agenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Agence introuvable pour l'utilisateur ID : " + userId));

        Car car = new Car();
        car.setMarque(carDto.getMarque());
        car.setModele(carDto.getModele());
        car.setAnnee(carDto.getAnnee());
        car.setType(carDto.getType());
        car.setCarburant(carDto.getCarburant());
        car.setTransmission(carDto.getTransmission());
        car.setSieges(carDto.getSieges());
        car.setPortes(carDto.getPortes());
        car.setClimatisation(carDto.isClimatisation());
        car.setPrixJour(carDto.getPrixJour());
        car.setCaution(carDto.getCaution());

        // Convertir MultipartFile en byte[]
        if (carDto.getImage() != null && !carDto.getImage().isEmpty()) {
            car.setImage(carDto.getImage().getBytes());
        }

        // Associer l’agence
        car.setAgence(agence);

        carRepository.save(car);
        return true;
    } catch (Exception e) {
        return false;
    }
}
    public List<CarDto> getCarsByUser(Long userId) {
        List<Car> cars = carRepository.findByAgenceUserId(userId);
        return cars.stream().map(this::convertToDto).toList();
    }

}
