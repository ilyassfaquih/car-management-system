package com.example.rentalservice.repository;

import com.example.rentalservice.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserId(String userId);
    List<Rental> findByCarIdAndReturnedFalse(Long carId);
    List<Rental> findByCarIdAndReturnedFalseAndEndDateAfterAndStartDateBefore(Long carId, LocalDate startDate, LocalDate endDate);
}