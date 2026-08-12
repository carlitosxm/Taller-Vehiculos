import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Owner } from '../models/owner.model';
import { Vehicle } from '../models/vehicle.model';

@Injectable({ providedIn: 'root' })
export class OwnerService {
  private readonly baseUrl = `${environment.apiUrl}/owners`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Owner[]> {
    return this.http.get<Owner[]>(this.baseUrl);
  }

  getById(id: number): Observable<Owner> {
    return this.http.get<Owner>(`${this.baseUrl}/${id}`);
  }

  getVehicles(id: number): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.baseUrl}/${id}/vehicles`);
  }

  create(owner: Owner): Observable<Owner> {
    return this.http.post<Owner>(this.baseUrl, owner);
  }

  update(id: number, owner: Owner): Observable<Owner> {
    return this.http.put<Owner>(`${this.baseUrl}/${id}`, owner);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
