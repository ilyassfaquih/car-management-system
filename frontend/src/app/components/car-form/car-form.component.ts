import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CarService } from '../../services/car.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-car-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink], // Nsiti ReactiveFormsModule? Ha howa hna
  templateUrl: './car-form.component.html',
  styleUrls: ['./car-form.component.css']
})
export class CarFormComponent implements OnInit {
  carForm: FormGroup;
  isEditMode = false;
  isLoading = false;
  errorMessage = '';
  carId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private carService: CarService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // 1. Définition dial l-Formulaire w Validation
    this.carForm = this.fb.group({
      brand: ['', [Validators.required, Validators.minLength(2)]],
      model: ['', [Validators.required]],
      year: [new Date().getFullYear(), [Validators.required, Validators.min(1900), Validators.max(new Date().getFullYear() + 1)]],
      registrationNumber: ['', [Validators.required]],
      pricePerDay: [0, [Validators.required, Validators.min(1)]],
      imageUrl: [''], // Optional
      available: [true]
    });
  }

  ngOnInit(): void {
    // Check ila l-user machi admin, rj3o l cars
    if (!this.authService.isAdmin()) {
      this.router.navigate(['/cars']);
      return;
    }

    // Check wach edit mode (ila kayn id f URL)
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.carId = +id;
      this.loadCarData(this.carId);
    }
  }

  loadCarData(id: number): void {
    this.isLoading = true;
    this.carService.getCarById(id).subscribe({
      next: (car) => {
        this.carForm.patchValue(car); // 3mmer l-formulaire b data
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Could not load car details';
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.carForm.invalid) return;

    this.isLoading = true;
    const carData = this.carForm.value;

    if (this.isEditMode && this.carId) {
      // UPDATE
      this.carService.updateCar(this.carId, carData).subscribe({
        next: () => this.router.navigate(['/cars']),
        error: (err) => {
          this.errorMessage = 'Failed to update car';
          this.isLoading = false;
        }
      });
    } else {
      // CREATE
      this.carService.createCar(carData).subscribe({
        next: () => this.router.navigate(['/cars']),
        error: (err) => {
          this.errorMessage = 'Failed to create car';
          this.isLoading = false;
        }
      });
    }
  }
}