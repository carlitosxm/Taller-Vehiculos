import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Vehicle } from '../models/vehicle.model';
import { MaintenanceOrder } from '../models/maintenance.model';

@Injectable({ providedIn: 'root' })
export class VehicleService {
  private readonly baseUrl = `${environment.apiUrl}/vehicles`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(this.baseUrl);
  }

  getById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.baseUrl}/${id}`);
  }

  getMaintenanceHistory(id: number): Observable<MaintenanceOrder[]> {
    return this.http.get<MaintenanceOrder[]>(`${this.baseUrl}/${id}/maintenance`);
  }

  create(vehicle: Vehicle): Observable<Vehicle> {
    return this.http.post<Vehicle>(this.baseUrl, vehicle);
  }

  update(id: number, vehicle: Vehicle): Observable<Vehicle> {
    return this.http.put<Vehicle>(`${this.baseUrl}/${id}`, vehicle);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
