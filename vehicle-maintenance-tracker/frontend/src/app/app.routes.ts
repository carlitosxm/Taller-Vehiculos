import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { OwnerListComponent } from './components/owner/owner-list.component';
import { VehicleListComponent } from './components/vehicle/vehicle-list.component';
import { MaintenanceListComponent } from './components/maintenance/maintenance-list.component';
import { MaintenanceHistoryComponent } from './components/maintenance/maintenance-history.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'owners', component: OwnerListComponent },
  { path: 'vehicles', component: VehicleListComponent },
  { path: 'vehicles/:id/history', component: MaintenanceHistoryComponent },
  { path: 'maintenance', component: MaintenanceListComponent },
  { path: '**', redirectTo: 'dashboard' },
];
