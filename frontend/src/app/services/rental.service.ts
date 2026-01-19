import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Rental } from '../models/rental.model';

@Injectable({
  providedIn: 'root'
})
export class RentalService {
  private apiUrl = `${environment.apiUrl}/rentals`;

  constructor(private http: HttpClient) {}

  createRental(rental: any): Observable<Rental> {
    return this.http.post<Rental>(this.apiUrl, rental);
  }

  getUserRentals(): Observable<Rental[]> {
    return this.http.get<Rental[]>(`${this.apiUrl}/user`);
  }

  getRentalById(id: number): Observable<Rental> {
    return this.http.get<Rental>(`${this.apiUrl}/${id}`);
  }

  returnCar(rentalId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${rentalId}/return`, {});
  }
}