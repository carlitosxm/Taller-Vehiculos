import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MaintenanceOrder, MaintenanceType } from '../../models/maintenance.model';
import { Vehicle } from '../../models/vehicle.model';
import { MaintenanceService } from '../../services/maintenance.service';
import { VehicleService } from '../../services/vehicle.service';

@Component({
  selector: 'app-maintenance-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="submit()" class="grid grid-cols-2 gap-3 bg-white p-4 rounded-lg shadow mb-6">
      <select formControlName="vehicleId" class="border rounded px-3 py-2">
        <option [ngValue]="null" disabled>Seleccione vehículo</option>
        @for (vehicle of vehicles; track vehicle.id) {
          <option [ngValue]="vehicle.id">{{ vehicle.placa }}</option>
        }
      </select>
      <select formControlName="maintenanceTypeId" class="border rounded px-3 py-2">
        <option [ngValue]="null" disabled>Seleccione tipo</option>
        @for (type of types; track type.id) {
          <option [ngValue]="type.id">{{ type.nombre }}</option>
        }
      </select>
      <input formControlName="fechaProgramada" type="date" class="border rounded px-3 py-2" />
      <input formControlName="costo" type="number" step="0.01" placeholder="Costo" class="border rounded px-3 py-2" />
      <input formControlName="tecnicoResponsable" placeholder="Técnico responsable" class="border rounded px-3 py-2" />
      <select formControlName="estado" class="border rounded px-3 py-2">
        <option value="PENDING">Pendiente</option>
        <option value="IN_PROCESS">En Proceso</option>
        <option value="COMPLETED">Finalizado</option>
      </select>
      <textarea formControlName="descripcion" placeholder="Descripción" class="border rounded px-3 py-2 col-span-2"></textarea>
      @if (error()) {
        <p class="col-span-2 text-red-600 text-sm">{{ error() }}</p>
      }
      <div class="col-span-2 flex gap-2">
        <button type="submit" [disabled]="form.invalid" class="bg-sky-600 text-white px-4 py-2 rounded disabled:opacity-50">
          {{ editingId ? 'Actualizar' : 'Crear' }} Orden
        </button>
        @if (editingId) {
          <button type="button" (click)="cancel()" class="bg-gray-300 px-4 py-2 rounded">Cancelar</button>
        }
      </div>
    </form>
  `,
})
export class MaintenanceFormComponent implements OnInit, OnChanges {
  @Input() orderToEdit: MaintenanceOrder | null = null;
  @Output() saved = new EventEmitter<void>();

  vehicles: Vehicle[] = [];
  types: MaintenanceType[] = [];
  editingId: number | null = null;
  error = () => this._error;
  private _error = '';

  private fb = inject(FormBuilder);
  private maintenanceService = inject(MaintenanceService);
  private vehicleService = inject(VehicleService);

  form = this.fb.group({
    vehicleId: [null as number | null, Validators.required],
    maintenanceTypeId: [null as number | null, Validators.required],
    fechaProgramada: [''],
    costo: [null as number | null, [Validators.min(0)]],
    tecnicoResponsable: [''],
    estado: ['PENDING'],
    descripcion: [''],
  });

  ngOnInit(): void {
    this.vehicleService.getAll().subscribe((vehicles) => (this.vehicles = vehicles));
    this.maintenanceService.getTypes().subscribe((types) => (this.types = types));
  }

  ngOnChanges(): void {
    if (this.orderToEdit) {
      this.editingId = this.orderToEdit.id ?? null;
      this.form.patchValue(this.orderToEdit);
    }
  }

  submit(): void {
    const value = this.form.getRawValue() as MaintenanceOrder;
    const request = this.editingId
      ? this.maintenanceService.update(this.editingId, value)
      : this.maintenanceService.create(value);

    request.subscribe({
      next: () => {
        this._error = '';
        this.form.reset({ estado: 'PENDING' });
        this.editingId = null;
        this.saved.emit();
      },
      error: (err) => (this._error = err.error?.message ?? 'Error al guardar la orden'),
    });
  }

  cancel(): void {
    this.editingId = null;
    this.form.reset({ estado: 'PENDING' });
    this.saved.emit();
  }
}
