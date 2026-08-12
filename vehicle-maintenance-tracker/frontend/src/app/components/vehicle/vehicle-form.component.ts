import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Vehicle } from '../../models/vehicle.model';
import { Owner } from '../../models/owner.model';
import { VehicleService } from '../../services/vehicle.service';
import { OwnerService } from '../../services/owner.service';

@Component({
  selector: 'app-vehicle-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="submit()" class="grid grid-cols-2 gap-3 bg-white p-4 rounded-lg shadow mb-6">
      <input formControlName="placa" placeholder="Placa (ABC-1234)" class="border rounded px-3 py-2" />
      <input formControlName="marca" placeholder="Marca" class="border rounded px-3 py-2" />
      <input formControlName="modelo" placeholder="Modelo" class="border rounded px-3 py-2" />
      <input formControlName="anio" type="number" placeholder="Año" class="border rounded px-3 py-2" />
      <select formControlName="ownerId" class="border rounded px-3 py-2">
        <option [ngValue]="null" disabled>Seleccione propietario</option>
        @for (owner of owners; track owner.id) {
          <option [ngValue]="owner.id">{{ owner.nombre }} {{ owner.apellido }}</option>
        }
      </select>
      <select formControlName="estado" class="border rounded px-3 py-2">
        <option value="ACTIVO">Activo</option>
        <option value="INACTIVO">Inactivo</option>
      </select>
      @if (error()) {
        <p class="col-span-2 text-red-600 text-sm">{{ error() }}</p>
      }
      <div class="col-span-2 flex gap-2">
        <button type="submit" [disabled]="form.invalid" class="bg-sky-600 text-white px-4 py-2 rounded disabled:opacity-50">
          {{ editingId ? 'Actualizar' : 'Crear' }} Vehículo
        </button>
        @if (editingId) {
          <button type="button" (click)="cancel()" class="bg-gray-300 px-4 py-2 rounded">Cancelar</button>
        }
      </div>
    </form>
  `,
})
export class VehicleFormComponent implements OnInit, OnChanges {
  @Input() vehicleToEdit: Vehicle | null = null;
  @Output() saved = new EventEmitter<void>();

  owners: Owner[] = [];
  editingId: number | null = null;
  error = () => this._error;
  private _error = '';

  private fb = inject(FormBuilder);
  private vehicleService = inject(VehicleService);
  private ownerService = inject(OwnerService);

  form = this.fb.group({
    placa: ['', [Validators.required, Validators.pattern(/^[A-Z]{3}-\d{3,4}$/)]],
    marca: ['', [Validators.required, Validators.minLength(2)]],
    modelo: [''],
    anio: [null as number | null],
    ownerId: [null as number | null, Validators.required],
    estado: ['ACTIVO'],
  });

  ngOnInit(): void {
    this.ownerService.getAll().subscribe((owners) => (this.owners = owners));
  }

  ngOnChanges(): void {
    if (this.vehicleToEdit) {
      this.editingId = this.vehicleToEdit.id ?? null;
      this.form.patchValue(this.vehicleToEdit);
    }
  }

  submit(): void {
    const value = this.form.getRawValue() as Vehicle;
    const request = this.editingId
      ? this.vehicleService.update(this.editingId, value)
      : this.vehicleService.create(value);

    request.subscribe({
      next: () => {
        this._error = '';
        this.form.reset({ estado: 'ACTIVO' });
        this.editingId = null;
        this.saved.emit();
      },
      error: (err) => (this._error = err.error?.message ?? 'Error al guardar el vehículo'),
    });
  }

  cancel(): void {
    this.editingId = null;
    this.form.reset({ estado: 'ACTIVO' });
    this.saved.emit();
  }
}
