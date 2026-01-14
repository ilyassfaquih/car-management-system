package com.example.rentalservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class MultiRentalRequest {
    @NotEmpty
    @Valid
    private List<RentalRequest> rentals;
}