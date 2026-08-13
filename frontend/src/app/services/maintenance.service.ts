import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MaintenanceOrder, MaintenanceType, OrderStatus } from '../models/maintenance.model';

@Injectable({ providedIn: 'root' })
export class MaintenanceService {
  private readonly ordersUrl = `${environment.apiUrl}/maintenance-orders`;
  private readonly typesUrl = `${environment.apiUrl}/maintenance-types`;

  constructor(private http: HttpClient) {}

  getTypes(): Observable<MaintenanceType[]> {
    return this.http.get<MaintenanceType[]>(this.typesUrl);
  }

  getAllOrders(): Observable<MaintenanceOrder[]> {
    return this.http.get<MaintenanceOrder[]>(this.ordersUrl);
  }

  getOrdersByStatus(status: OrderStatus): Observable<MaintenanceOrder[]> {
    const params = new HttpParams().set('status', status);
    return this.http.get<MaintenanceOrder[]>(this.ordersUrl, { params });
  }

  create(order: MaintenanceOrder): Observable<MaintenanceOrder> {
    return this.http.post<MaintenanceOrder>(this.ordersUrl, order);
  }

  update(id: number, order: MaintenanceOrder): Observable<MaintenanceOrder> {
    return this.http.put<MaintenanceOrder>(`${this.ordersUrl}/${id}`, order);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.ordersUrl}/${id}`);
  }
}
