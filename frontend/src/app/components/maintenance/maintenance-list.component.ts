import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MaintenanceOrder, OrderStatus } from '../../models/maintenance.model';
import { MaintenanceService } from '../../services/maintenance.service';
import { MaintenanceFormComponent } from './maintenance-form.component';

@Component({
  selector: 'app-maintenance-list',
  standalone: true,
  imports: [CommonModule, FormsModule, MaintenanceFormComponent],
  template: `
    <h1 class="text-2xl font-bold mb-4">Órdenes de Mantenimiento</h1>
    <app-maintenance-form [orderToEdit]="selectedOrder" (saved)="onSaved()"></app-maintenance-form>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <section>
        <h2 class="font-bold text-amber-600 mb-2">⏳ Pendientes ({{ pending.length }})</h2>
        @for (order of pending; track order.id) {
          <ng-container *ngTemplateOutlet="card; context: { order }"></ng-container>
        }
      </section>
      <section>
        <h2 class="font-bold text-blue-600 mb-2">🔧 En Proceso ({{ inProcess.length }})</h2>
        @for (order of inProcess; track order.id) {
          <ng-container *ngTemplateOutlet="card; context: { order }"></ng-container>
        }
      </section>
      <section>
        <h2 class="font-bold text-green-600 mb-2">✅ Finalizadas ({{ completed.length }})</h2>
        @for (order of completed; track order.id) {
          <ng-container *ngTemplateOutlet="card; context: { order }"></ng-container>
        }
      </section>
    </div>

    <ng-template #card let-order="order">
      <div class="bg-white rounded-lg shadow p-3 mb-3">
        <p class="font-semibold">{{ order.vehiclePlaca }} — {{ order.maintenanceTypeNombre }}</p>
        <p class="text-sm text-gray-500">Programado: {{ order.fechaProgramada || '-' }}</p>
        <p class="text-sm text-gray-500">Costo: {{ order.costo ?? 0 | currency }}</p>
        <div class="mt-2 flex items-center gap-2">
          <select
            [ngModel]="order.estado"
            (ngModelChange)="changeStatus(order, $event)"
            class="border rounded px-2 py-1 text-sm"
          >
            <option value="PENDING">Pendiente</option>
            <option value="IN_PROCESS">En Proceso</option>
            <option value="COMPLETED">Finalizado</option>
          </select>
          <button (click)="edit(order)" class="text-sky-600 hover:underline text-sm">Editar</button>
          <button (click)="remove(order)" class="text-red-600 hover:underline text-sm">Eliminar</button>
        </div>
      </div>
    </ng-template>
  `,
})
export class MaintenanceListComponent implements OnInit {
  pending: MaintenanceOrder[] = [];
  inProcess: MaintenanceOrder[] = [];
  completed: MaintenanceOrder[] = [];
  selectedOrder: MaintenanceOrder | null = null;

  constructor(private maintenanceService: MaintenanceService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.maintenanceService.getOrdersByStatus('PENDING').subscribe((orders) => (this.pending = orders));
    this.maintenanceService.getOrdersByStatus('IN_PROCESS').subscribe((orders) => (this.inProcess = orders));
    this.maintenanceService.getOrdersByStatus('COMPLETED').subscribe((orders) => (this.completed = orders));
  }

  changeStatus(order: MaintenanceOrder, estado: OrderStatus): void {
    this.maintenanceService.update(order.id!, { ...order, estado }).subscribe(() => this.load());
  }

  edit(order: MaintenanceOrder): void {
    this.selectedOrder = { ...order };
  }

  remove(order: MaintenanceOrder): void {
    if (confirm(`¿Eliminar la orden de ${order.vehiclePlaca}?`)) {
      this.maintenanceService.delete(order.id!).subscribe(() => this.load());
    }
  }

  onSaved(): void {
    this.selectedOrder = null;
    this.load();
  }
}
