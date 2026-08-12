import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardAlert, DashboardCost, DashboardSummary } from '../../models/maintenance.model';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h1 class="text-2xl font-bold mb-4">Dashboard</h1>

    @if (summary) {
      <div class="grid grid-cols-2 md:grid-cols-5 gap-4 mb-6">
        <div class="bg-white rounded-lg shadow p-4 text-center">
          <p class="text-2xl font-bold">{{ summary.totalVehicles }}</p>
          <p class="text-sm text-gray-500">Vehículos</p>
        </div>
        <div class="bg-white rounded-lg shadow p-4 text-center">
          <p class="text-2xl font-bold">{{ summary.totalOwners }}</p>
          <p class="text-sm text-gray-500">Propietarios</p>
        </div>
        <div class="bg-amber-50 rounded-lg shadow p-4 text-center">
          <p class="text-2xl font-bold text-amber-600">{{ summary.pendingOrders }}</p>
          <p class="text-sm text-gray-500">Pendientes</p>
        </div>
        <div class="bg-blue-50 rounded-lg shadow p-4 text-center">
          <p class="text-2xl font-bold text-blue-600">{{ summary.inProcessOrders }}</p>
          <p class="text-sm text-gray-500">En Proceso</p>
        </div>
        <div class="bg-green-50 rounded-lg shadow p-4 text-center">
          <p class="text-2xl font-bold text-green-600">{{ summary.completedOrders }}</p>
          <p class="text-sm text-gray-500">Finalizadas</p>
        </div>
      </div>
    }

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <section class="bg-white rounded-lg shadow p-4">
        <h2 class="font-bold mb-3">🔔 Alertas de próximo mantenimiento</h2>
        @if (!alerts.length) {
          <p class="text-sm text-gray-400">Sin alertas próximas</p>
        }
        @for (alert of alerts; track alert.orderId) {
          <div
            class="flex justify-between items-center p-2 rounded mb-2"
            [class.bg-red-50]="alert.urgencia === 'URGENTE'"
            [class.bg-amber-50]="alert.urgencia === 'PROXIMO'"
          >
            <span>{{ alert.vehiclePlaca }} — {{ alert.maintenanceTypeNombre }}</span>
            <span class="text-sm">{{ alert.fechaProgramada }}</span>
          </div>
        }
      </section>

      <section class="bg-white rounded-lg shadow p-4">
        <h2 class="font-bold mb-3">💰 Gasto por vehículo (últimos 30 días)</h2>
        @if (!costs.length) {
          <p class="text-sm text-gray-400">Sin gastos registrados</p>
        }
        @for (cost of costs; track cost.vehicleId) {
          <div class="mb-2">
            <div class="flex justify-between text-sm">
              <span>{{ cost.vehiclePlaca }}</span>
              <span>{{ cost.totalCosto | currency }}</span>
            </div>
            <div class="w-full bg-gray-100 rounded h-2">
              <div class="bg-sky-500 h-2 rounded" [style.width.%]="barWidth(cost.totalCosto)"></div>
            </div>
          </div>
        }
      </section>
    </div>
  `,
})
export class DashboardComponent implements OnInit {
  summary: DashboardSummary | null = null;
  alerts: DashboardAlert[] = [];
  costs: DashboardCost[] = [];

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getSummary().subscribe((summary) => (this.summary = summary));
    this.dashboardService.getAlerts().subscribe((alerts) => (this.alerts = alerts));
    this.dashboardService.getCosts().subscribe((costs) => (this.costs = costs));
  }

  barWidth(value: number): number {
    const max = Math.max(...this.costs.map((c) => c.totalCosto), 1);
    return (value / max) * 100;
  }
}
