import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CarService } from '../../services/car.service';
import { RentalService } from '../../services/rental.service';
import { Car } from '../../models/car.model';

@Component({
  selector: 'app-cars-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cars-list.component.html',
  styleUrls: ['./cars-list.component.css']
})
export class CarsListComponent implements OnInit {
  cars: Car[] = [];
  isLoading = true;
  errorMessage = '';
  successMessage = '';

  constructor(
    private carService: CarService,
    private rentalService: RentalService
  ) {}

  ngOnInit(): void {
    this.loadCars();
  }

  loadCars(): void {
    this.isLoading = true;
    this.carService.getAllCars().subscribe({
      next: (cars) => {
        this.cars = cars;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load cars';
        this.isLoading = false;
        console.error('Error loading cars:', error);
      }
    });
  }

  rentCar(car: Car): void {
    if (confirm(`Rent ${car.brand} ${car.model} for $${car.pricePerDay}/day?`)) {
      const rental = {
        carId: car.id,
        startDate: new Date().toISOString(),
        endDate: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString() // 7 days
      };

      this.rentalService.createRental(rental).subscribe({
        next: (response) => {
          this.successMessage = `Successfully rented ${car.brand} ${car.model}!`;
          setTimeout(() => this.successMessage = '', 3000);
          this.loadCars(); // Reload to update availability
        },
        error: (error) => {
          this.errorMessage = 'Failed to rent car. Please try again.';
          setTimeout(() => this.errorMessage = '', 3000);
          console.error('Error renting car:', error);
        }
      });
    }
  }
}