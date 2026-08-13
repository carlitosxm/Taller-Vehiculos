import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Owner } from '../../models/owner.model';
import { OwnerService } from '../../services/owner.service';
import { OwnerFormComponent } from './owner-form.component';

@Component({
  selector: 'app-owner-list',
  standalone: true,
  imports: [CommonModule, FormsModule, OwnerFormComponent],
  template: `
    <h1 class="text-2xl font-bold mb-4">Propietarios</h1>
    <app-owner-form [ownerToEdit]="selectedOwner" (saved)="onSaved()"></app-owner-form>

    <input
      [(ngModel)]="search"
      placeholder="Buscar por nombre, apellido o cédula/RUC"
      class="border rounded px-3 py-2 mb-4 w-full"
    />

    <table class="w-full bg-white rounded-lg shadow overflow-hidden">
      <thead class="bg-slate-100 text-left">
        <tr>
          <th class="p-3">Nombre</th>
          <th class="p-3">Cédula/RUC</th>
          <th class="p-3">Teléfono</th>
          <th class="p-3">Vehículos</th>
          <th class="p-3">Acciones</th>
        </tr>
      </thead>
      <tbody>
        @for (owner of filteredOwners(); track owner.id) {
          <tr class="border-t">
            <td class="p-3">{{ owner.nombre }} {{ owner.apellido }}</td>
            <td class="p-3">{{ owner.cedulaORuc }}</td>
            <td class="p-3">{{ owner.telefono || '-' }}</td>
            <td class="p-3">{{ vehicleCounts[owner.id!] || 0 }}</td>
            <td class="p-3 flex gap-2">
              <button (click)="edit(owner)" class="text-sky-600 hover:underline">Editar</button>
              <button (click)="remove(owner)" class="text-red-600 hover:underline">Eliminar</button>
            </td>
          </tr>
        }
      </tbody>
    </table>
  `,
})
export class OwnerListComponent implements OnInit {
  owners: Owner[] = [];
  vehicleCounts: Record<number, number> = {};
  selectedOwner: Owner | null = null;
  search = '';

  constructor(private ownerService: OwnerService) {}

  ngOnInit(): void {
    this.load();
  }

  filteredOwners(): Owner[] {
    const term = this.search.trim().toLowerCase();
    if (!term) return this.owners;
    return this.owners.filter((o) =>
      `${o.nombre} ${o.apellido} ${o.cedulaORuc}`.toLowerCase().includes(term)
    );
  }

  load(): void {
    this.ownerService.getAll().subscribe((owners) => {
      this.owners = owners;
      owners.forEach((owner) => {
        this.ownerService.getVehicles(owner.id!).subscribe((vehicles) => {
          this.vehicleCounts[owner.id!] = vehicles.length;
        });
      });
    });
  }

  edit(owner: Owner): void {
    this.selectedOwner = { ...owner };
  }

  remove(owner: Owner): void {
    if (confirm(`¿Eliminar al propietario ${owner.nombre} ${owner.apellido}?`)) {
      this.ownerService.delete(owner.id!).subscribe(() => this.load());
    }
  }

  onSaved(): void {
    this.selectedOwner = null;
    this.load();
  }
}
