import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Car } from '../models/car.model';

@Injectable({
  providedIn: 'root'
})
export class CarService {
  private apiUrl = `${environment.apiUrl}/cars`;

  constructor(private http: HttpClient) {}

  getAllCars(): Observable<Car[]> {
    return this.http.get<Car[]>(this.apiUrl);
  }

  getCarById(id: number): Observable<Car> {
    return this.http.get<Car>(`${this.apiUrl}/${id}`);
  }

  getAvailableCars(): Observable<Car[]> {
    return this.http.get<Car[]>(`${this.apiUrl}/available`);
  }
}