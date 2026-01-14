package com.example.carservice.service;

import com.example.carservice.dto.CarRequest;
import com.example.carservice.model.Car;
import com.example.carservice.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car addCar(CarRequest request) {
        Car car = Car.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .owner(request.getOwner())
                .available(true)
                .build();
        return carRepository.save(car);
    }

    public Car updateCar(Long id, CarRequest request) {
        Car car = carRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Car not found"));
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setOwner(request.getOwner());
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Car not found"));
        if (!car.isAvailable()) {
            throw new IllegalStateException("Cannot delete a car that is currently rented");
        }
        carRepository.delete(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> searchCars(String brand, String model, Integer year) {
        return carRepository.findAll().stream()
                .filter(car -> brand == null || car.getBrand().equalsIgnoreCase(brand))
                .filter(car -> model == null || car.getModel().equalsIgnoreCase(model))
                .filter(car -> year == null || car.getYear() == year)
                .collect(Collectors.toList());
    }

    public void updateAvailability(Long id, boolean available) {
        Car car = carRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Car not found"));
        car.setAvailable(available);
        if (available) {
            // When returned, increase popularity (rental count) by 1
            car.setPopularity(car.getPopularity() + 1);
        }
        carRepository.save(car);
    }

    public List<Car> recommendCars() {
        return carRepository.findAll().stream()
                // prefer available cars
                .filter(Car::isAvailable)
                // sort by popularity descending then by year descending (newer first)
                .sorted(Comparator.comparingInt(Car::getPopularity).reversed()
                        .thenComparing(Car::getYear, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}