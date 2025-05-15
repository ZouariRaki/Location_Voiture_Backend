package com.projet.locationvoiture.services.cars;

import com.projet.locationvoiture.dto.CarDto;
import com.projet.locationvoiture.entity.Car;
import com.projet.locationvoiture.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

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

    public List<Car> getAllCars() {
        return carRepository.findAll();
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
}
