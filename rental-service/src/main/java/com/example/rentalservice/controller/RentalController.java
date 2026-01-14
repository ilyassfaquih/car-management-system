package com.example.rentalservice.controller;

import com.example.rentalservice.dto.MultiRentalRequest;
import com.example.rentalservice.dto.RentalRequest;
import com.example.rentalservice.model.Rental;
import com.example.rentalservice.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public ResponseEntity<?> rentCar(@RequestBody @Valid RentalRequest request, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        try {
            Rental rental = rentalService.rentCar(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(rental);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/multi")
    public ResponseEntity<?> rentMultiple(@RequestBody @Valid MultiRentalRequest request, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        try {
            List<Rental> rentals = rentalService.rentMultiple(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(rentals);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<?> returnCar(@PathVariable Long id,
                                      @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {
        try {
            Rental rental = rentalService.returnCar(id, returnDate);
            return ResponseEntity.ok(rental);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<Rental>> myRentals(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(rentalService.getMyRentals(userId));
    }

    @GetMapping
    public ResponseEntity<List<Rental>> allRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }
}