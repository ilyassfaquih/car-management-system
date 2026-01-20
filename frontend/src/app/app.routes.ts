import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { CarsListComponent } from './components/cars-list/cars-list.component';
import { RentalsComponent } from './components/rentals/rentals.component';
import { CarFormComponent } from './components/car-form/car-form.component'; // <-- Import Mohim
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  // Redirect root to login
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  
  // Public Route
  { path: 'login', component: LoginComponent },
  
  // Protected Routes (Admin & User)
  { path: 'cars', component: CarsListComponent, canActivate: [authGuard] },
  { path: 'rentals', component: RentalsComponent, canActivate: [authGuard] },
  
  // 👇 Hado homa li kano naqsin
  { path: 'add-car', component: CarFormComponent, canActivate: [authGuard] },
  { path: 'edit-car/:id', component: CarFormComponent, canActivate: [authGuard] },

  // Wildcard (ila dkhl chi wahed l chi lien mkhra9)
  { path: '**', redirectTo: '/login' }
];