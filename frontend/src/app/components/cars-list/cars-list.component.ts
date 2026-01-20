import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // Import Router
import { CarService } from '../../services/car.service';
import { RentalService } from '../../services/rental.service';
import { AuthService } from '../../services/auth.service'; // <-- HADI LI KANT NAQSA
import { Car } from '../../models/car.model';

@Component({
  selector: 'app-cars-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cars-list.component.html',
  styleUrls: ['./cars-list.component.css']
})
export class CarsListComponent implements OnInit {
  // Déclaration d les variables (Darouriyin)
  cars: Car[] = [];
  isLoading = true;
  errorMessage = '';
  successMessage = '';
  isAdmin = false;

  constructor(
    private carService: CarService,
    private rentalService: RentalService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Vérifier wach admin bach t-affichi les boutons
    this.isAdmin = this.authService.isAdmin();
    this.loadCars();
  }

  loadCars(): void {
    this.isLoading = true;
    this.carService.getAllCars().subscribe({
      next: (cars) => {
        this.cars = cars;
        this.isLoading = false;
      },
      error: (error: any) => {
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
          this.loadCars();
        },
        error: (error: any) => {
          this.errorMessage = 'Failed to rent car. Please try again.';
          setTimeout(() => this.errorMessage = '', 3000);
          console.error('Error renting car:', error);
        }
      });
    }
  }

  deleteCar(car: Car): void {
    if (confirm('Are you sure you want to delete this car?')) {
      // T-akked anaka zedti deleteCar f CarService.ts
      this.carService.deleteCar(car.id).subscribe({
        next: () => {
          this.successMessage = 'Car deleted successfully';
          this.loadCars(); // Recharger la liste
        },
        error: (err: any) => {
          this.errorMessage = 'Could not delete car';
          console.error(err);
        }
      });
    }
  }

  // Hado bach l-HTML ma y-dirch erreur, ta t-sawb les components dialhom mn be3d
  navigateToAdd(): void {
    console.log("Navigate to add car page");
    // this.router.navigate(['/add-car']);
  }

  navigateToEdit(car: Car): void {
    console.log("Navigate to edit car page", car);
    // this.router.navigate(['/edit-car', car.id]);
  }
}