package com.example.rentalservice.service;

import com.example.rentalservice.dto.MultiRentalRequest;
import com.example.rentalservice.dto.RentalRequest;
import com.example.rentalservice.model.Rental;
import com.example.rentalservice.repository.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RentalService {

    private final RentalRepository rentalRepository;
    private final RestTemplate restTemplate;
    private final String carServiceUrl;

    public RentalService(RentalRepository rentalRepository,
                         RestTemplate restTemplate,
                         @Value("${car.service.url}") String carServiceUrl) {
        this.rentalRepository = rentalRepository;
        this.restTemplate = restTemplate;
        this.carServiceUrl = carServiceUrl;
    }

    /**
     * Rent a single car. This method checks availability via car service, prevents double booking,
     * marks the car as unavailable and records the rental.
     */
    public Rental rentCar(RentalRequest request, String userId) {
        // Validate dates
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        // Check car availability by fetching car details
        String url = carServiceUrl + "/" + request.getCarId();
        ResponseEntity<CarDto> carResponse = restTemplate.getForEntity(url, CarDto.class);
        if (!carResponse.getStatusCode().is2xxSuccessful() || carResponse.getBody() == null) {
            throw new IllegalArgumentException("Car not found");
        }
        CarDto carDto = carResponse.getBody();
        if (!carDto.isAvailable()) {
            throw new IllegalStateException("Car is not available");
        }
        // Check overlapping rentals
        List<Rental> overlapping = rentalRepository.findByCarIdAndReturnedFalseAndEndDateAfterAndStartDateBefore(
                request.getCarId(), request.getStartDate(), request.getEndDate());
        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("Car already rented during this period");
        }
        // Calculate total price (simple example: 50 per day, plus dynamic factor based on popularity)
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        BigDecimal baseRate = BigDecimal.valueOf(50);
        BigDecimal popularityFactor = BigDecimal.valueOf(1 + carDto.getPopularity() / 10.0);
        BigDecimal totalPrice = baseRate.multiply(BigDecimal.valueOf(days)).multiply(popularityFactor);
        // Mark car as unavailable
        String availabilityUrl = carServiceUrl + "/" + request.getCarId() + "/availability?available=false";
        restTemplate.patchForObject(availabilityUrl, null, Void.class);
        // Save rental
        Rental rental = Rental.builder()
                .carId(request.getCarId())
                .userId(userId)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .returned(false)
                .totalPrice(totalPrice)
                .build();
        return rentalRepository.save(rental);
    }

    /**
     * Rent multiple cars in a single transaction.
     */
    public List<Rental> rentMultiple(MultiRentalRequest request, String userId) {
        List<Rental> rentals = new ArrayList<>();
        for (RentalRequest rr : request.getRentals()) {
            rentals.add(rentCar(rr, userId));
        }
        return rentals;
    }

    /**
     * Return a rented car. Marks rental as returned, calculates penalty if returned late, and marks car as available.
     */
    public Rental returnCar(Long rentalId, LocalDate actualReturnDate) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() -> new IllegalArgumentException("Rental not found"));
        if (rental.isReturned()) {
            throw new IllegalStateException("Rental already returned");
        }
        rental.setReturned(true);
        // Calculate late fee: if actual return date is after endDate, add 20 per late day
        long lateDays = ChronoUnit.DAYS.between(rental.getEndDate(), actualReturnDate);
        if (lateDays > 0) {
            BigDecimal lateFee = BigDecimal.valueOf(lateDays).multiply(BigDecimal.valueOf(20));
            rental.setTotalPrice(rental.getTotalPrice().add(lateFee));
        }
        rentalRepository.save(rental);
        // Mark car available
        String availabilityUrl = carServiceUrl + "/" + rental.getCarId() + "/availability?available=true";
        restTemplate.patchForObject(availabilityUrl, null, Void.class);
        return rental;
    }

    /**
     * Get rentals for the current user.
     */
    public List<Rental> getMyRentals(String userId) {
        return rentalRepository.findByUserId(userId);
    }

    /**
     * Get all rentals (admin only).
     */
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Simple DTO to hold car information returned by the car service.
     */
    private static class CarDto {
        private Long id;
        private String brand;
        private String model;
        private int year;
        private String owner;
        private boolean available;
        private int popularity;

        public boolean isAvailable() { return available; }
        public int getPopularity() { return popularity; }
    }
}