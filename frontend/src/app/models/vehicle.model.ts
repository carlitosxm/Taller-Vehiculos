export type VehicleStatus = 'ACTIVO' | 'INACTIVO';

export interface Vehicle {
  id?: number;
  placa: string;
  marca: string;
  modelo?: string;
  anio?: number;
  ownerId: number;
  ownerNombreCompleto?: string;
  estado?: VehicleStatus;
  fechaRegistro?: string;
}
