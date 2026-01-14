package com.example.carservice.controller;

import com.example.carservice.dto.CarRequest;
import com.example.carservice.model.Car;
import com.example.carservice.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<?> addCar(@RequestBody @Valid CarRequest request) {
        try {
            Car created = carService.addCar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // ✅ CORRECTION HNA: Zidna ("id")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable("id") Long id, @RequestBody @Valid CarRequest request) {
        try {
            Car updated = carService.updateCar(id, request);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // ✅ CORRECTION HNA: Zidna ("id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable("id") Long id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    // ✅ CORRECTION HNA (Hadi li daret l-erreur): Zidna ("id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCar(@PathVariable("id") Long id) {
        try {
            Car car = carService.getAllCars().stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Car not found"));
            return ResponseEntity.ok(car);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Car>> searchCars(@RequestParam(required = false) String brand,
                                                @RequestParam(required = false) String model,
                                                @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(carService.searchCars(brand, model, year));
    }

    // ✅ CORRECTION HNA: Zidna ("id")
    @PatchMapping("/{id}/availability")
    public ResponseEntity<?> updateAvailability(@PathVariable("id") Long id,
                                                @RequestParam boolean available) {
        try {
            carService.updateAvailability(id, available);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<Car>> recommendCars() {
        return ResponseEntity.ok(carService.recommendCars());
    }
}