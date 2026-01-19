import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RentalService } from '../../services/rental.service';
import { Rental } from '../../models/rental.model';

@Component({
  selector: 'app-rentals',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './rentals.component.html',
  styleUrls: ['./rentals.component.css']
})
export class RentalsComponent implements OnInit {
  rentals: Rental[] = [];
  isLoading = true;
  errorMessage = '';
  successMessage = '';

  constructor(private rentalService: RentalService) {}

  ngOnInit(): void {
    this.loadRentals();
  }

  loadRentals(): void {
    this.isLoading = true;
    this.rentalService.getUserRentals().subscribe({
      next: (rentals) => {
        this.rentals = rentals;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load rentals';
        this.isLoading = false;
        console.error('Error loading rentals:', error);
      }
    });
  }

  returnCar(rental: Rental): void {
    if (confirm('Are you sure you want to return this car?')) {
      this.rentalService.returnCar(rental.id).subscribe({
        next: () => {
          this.successMessage = 'Car returned successfully!';
          setTimeout(() => this.successMessage = '', 3000);
          this.loadRentals();
        },
        error: (error) => {
          this.errorMessage = 'Failed to return car. Please try again.';
          setTimeout(() => this.errorMessage = '', 3000);
          console.error('Error returning car:', error);
        }
      });
    }
  }

  calculateDays(startDate: string, endDate: string): number {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const diff = end.getTime() - start.getTime();
    return Math.ceil(diff / (1000 * 60 * 60 * 24));
  }
}