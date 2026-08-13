export type OrderStatus = 'PENDING' | 'IN_PROCESS' | 'COMPLETED';

export interface MaintenanceType {
  id?: number;
  nombre: string;
  descripcion?: string;
}

export interface MaintenanceOrder {
  id?: number;
  vehicleId: number;
  vehiclePlaca?: string;
  maintenanceTypeId: number;
  maintenanceTypeNombre?: string;
  descripcion?: string;
  estado?: OrderStatus;
  fechaProgramada?: string;
  fechaRealizacion?: string;
  costo?: number;
  tecnicoResponsable?: string;
  notas?: string;
  fechaCreacion?: string;
}

export interface DashboardSummary {
  totalVehicles: number;
  totalOwners: number;
  pendingOrders: number;
  inProcessOrders: number;
  completedOrders: number;
}

export interface DashboardAlert {
  orderId: number;
  vehiclePlaca: string;
  maintenanceTypeNombre: string;
  fechaProgramada: string;
  urgencia: 'URGENTE' | 'PROXIMO';
}

export interface DashboardCost {
  vehicleId: number;
  vehiclePlaca: string;
  totalCosto: number;
}
