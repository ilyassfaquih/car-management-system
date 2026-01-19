import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { CarsListComponent } from './components/cars-list/cars-list.component';
import { RentalsComponent } from './components/rentals/rentals.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'cars', component: CarsListComponent, canActivate: [authGuard] },
  { path: 'rentals', component: RentalsComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '/login' }
];