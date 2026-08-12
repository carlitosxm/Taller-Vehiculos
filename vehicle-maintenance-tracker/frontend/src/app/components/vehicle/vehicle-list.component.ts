import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Vehicle } from '../../models/vehicle.model';
import { VehicleService } from '../../services/vehicle.service';
import { VehicleFormComponent } from './vehicle-form.component';

@Component({
  selector: 'app-vehicle-list',
  standalone: true,
  imports: [CommonModule, RouterLink, VehicleFormComponent],
  template: `
    <h1 class="text-2xl font-bold mb-4">Vehículos</h1>
    <app-vehicle-form [vehicleToEdit]="selectedVehicle" (saved)="onSaved()"></app-vehicle-form>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      @for (vehicle of vehicles; track vehicle.id) {
        <div class="bg-white rounded-lg shadow p-4">
          <div class="flex justify-between items-start">
            <h2 class="font-bold text-lg">{{ vehicle.placa }}</h2>
            <span
              class="text-xs px-2 py-1 rounded-full"
              [class.bg-green-100]="vehicle.estado === 'ACTIVO'"
              [class.text-green-700]="vehicle.estado === 'ACTIVO'"
              [class.bg-gray-200]="vehicle.estado === 'INACTIVO'"
              [class.text-gray-600]="vehicle.estado === 'INACTIVO'"
            >{{ vehicle.estado }}</span>
          </div>
          <p class="text-sm text-gray-600">{{ vehicle.marca }} {{ vehicle.modelo }} ({{ vehicle.anio }})</p>
          <p class="text-sm text-gray-500">Propietario: {{ vehicle.ownerNombreCompleto }}</p>
          <div class="mt-3 flex gap-3 text-sm">
            <a [routerLink]="['/vehicles', vehicle.id, 'history']" class="text-sky-600 hover:underline">Historial</a>
            <button (click)="edit(vehicle)" class="text-sky-600 hover:underline">Editar</button>
            <button (click)="remove(vehicle)" class="text-red-600 hover:underline">Eliminar</button>
          </div>
        </div>
      }
    </div>
  `,
})
export class VehicleListComponent implements OnInit {
  vehicles: Vehicle[] = [];
  selectedVehicle: Vehicle | null = null;

  constructor(private vehicleService: VehicleService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.vehicleService.getAll().subscribe((vehicles) => (this.vehicles = vehicles));
  }

  edit(vehicle: Vehicle): void {
    this.selectedVehicle = { ...vehicle };
  }

  remove(vehicle: Vehicle): void {
    if (confirm(`¿Eliminar el vehículo ${vehicle.placa}?`)) {
      this.vehicleService.delete(vehicle.id!).subscribe(() => this.load());
    }
  }

  onSaved(): void {
    this.selectedVehicle = null;
    this.load();
  }
}
