import { Component, EventEmitter, inject, Input, OnChanges, Output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Owner } from '../../models/owner.model';
import { OwnerService } from '../../services/owner.service';

@Component({
  selector: 'app-owner-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="submit()" class="grid grid-cols-2 gap-3 bg-white p-4 rounded-lg shadow mb-6">
      <input formControlName="nombre" placeholder="Nombre" class="border rounded px-3 py-2" />
      <input formControlName="apellido" placeholder="Apellido" class="border rounded px-3 py-2" />
      <input formControlName="cedulaORuc" placeholder="Cédula/RUC" class="border rounded px-3 py-2" />
      <input formControlName="telefono" placeholder="Teléfono" class="border rounded px-3 py-2" />
      <input formControlName="direccion" placeholder="Dirección" class="border rounded px-3 py-2 col-span-2" />
      @if (error()) {
        <p class="col-span-2 text-red-600 text-sm">{{ error() }}</p>
      }
      <div class="col-span-2 flex gap-2">
        <button type="submit" [disabled]="form.invalid" class="bg-sky-600 text-white px-4 py-2 rounded disabled:opacity-50">
          {{ editingId ? 'Actualizar' : 'Crear' }} Propietario
        </button>
        @if (editingId) {
          <button type="button" (click)="cancel()" class="bg-gray-300 px-4 py-2 rounded">Cancelar</button>
        }
      </div>
    </form>
  `,
})
export class OwnerFormComponent implements OnChanges {
  @Input() ownerToEdit: Owner | null = null;
  @Output() saved = new EventEmitter<void>();

  editingId: number | null = null;
  error = () => this._error;
  private _error = '';

  private fb = inject(FormBuilder);
  private ownerService = inject(OwnerService);

  form = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    apellido: ['', [Validators.required, Validators.minLength(2)]],
    cedulaORuc: ['', Validators.required],
    telefono: [''],
    direccion: [''],
  });

  ngOnChanges(): void {
    if (this.ownerToEdit) {
      this.editingId = this.ownerToEdit.id ?? null;
      this.form.patchValue(this.ownerToEdit);
    }
  }

  submit(): void {
    const value = this.form.getRawValue() as Owner;
    const request = this.editingId
      ? this.ownerService.update(this.editingId, value)
      : this.ownerService.create(value);

    request.subscribe({
      next: () => {
        this._error = '';
        this.form.reset();
        this.editingId = null;
        this.saved.emit();
      },
      error: (err) => (this._error = err.error?.message ?? 'Error al guardar el propietario'),
    });
  }

  cancel(): void {
    this.editingId = null;
    this.form.reset();
    this.saved.emit();
  }
}
