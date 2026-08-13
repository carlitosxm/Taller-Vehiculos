import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MaintenanceOrder } from '../../models/maintenance.model';
import { Vehicle } from '../../models/vehicle.model';
import { VehicleService } from '../../services/vehicle.service';

@Component({
  selector: 'app-maintenance-history',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <a routerLink="/vehicles" class="text-sky-600 hover:underline">&larr; Volver a vehículos</a>
    @if (vehicle) {
      <h1 class="text-2xl font-bold my-4">Historial — {{ vehicle.placa }} ({{ vehicle.marca }} {{ vehicle.modelo }})</h1>
      <p class="mb-4 font-semibold">Costo acumulado: {{ totalCosto() | currency }}</p>
      <ol class="relative border-l-2 border-slate-200 pl-6 space-y-6">
        @for (order of orders; track order.id) {
          <li>
            <div class="absolute -left-[9px] w-4 h-4 rounded-full bg-sky-600"></div>
            <p class="font-semibold">{{ order.maintenanceTypeNombre }} — {{ order.estado }}</p>
            <p class="text-sm text-gray-500">Programado: {{ order.fechaProgramada || '-' }} | Realizado: {{ order.fechaRealizacion || '-' }}</p>
            <p class="text-sm text-gray-500">Costo: {{ order.costo ?? 0 | currency }}</p>
            @if (order.notas) {
              <p class="text-sm text-gray-400">{{ order.notas }}</p>
            }
          </li>
        }
      </ol>
    }
  `,
})
export class MaintenanceHistoryComponent implements OnInit {
  vehicle: Vehicle | null = null;
  orders: MaintenanceOrder[] = [];

  constructor(private route: ActivatedRoute, private vehicleService: VehicleService) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.vehicleService.getById(id).subscribe((vehicle) => (this.vehicle = vehicle));
    this.vehicleService.getMaintenanceHistory(id).subscribe((orders) => (this.orders = orders));
  }

  totalCosto(): number {
    return this.orders.reduce((sum, order) => sum + (order.costo ?? 0), 0);
  }
}
