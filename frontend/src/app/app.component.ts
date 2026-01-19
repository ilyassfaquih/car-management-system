import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="navbar" *ngIf="authService.isAuthenticated()">
      <div class="navbar-content">
        <h1>🚗 Car Management System</h1>
        <nav>
          <a routerLink="/cars" routerLinkActive="active">Cars</a>
          <a routerLink="/rentals" routerLinkActive="active">My Rentals</a>
          <button class="btn btn-danger" (click)="logout()">Logout</button>
        </nav>
      </div>
    </div>
    <router-outlet></router-outlet>
  `
})
export class AppComponent {
  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}