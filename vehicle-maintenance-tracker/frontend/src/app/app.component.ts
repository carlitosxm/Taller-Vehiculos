import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav class="bg-slate-800 text-white px-6 py-3 flex items-center gap-6 shadow">
      <span class="font-bold text-lg">🚗 Vehicle Maintenance Tracker</span>
      <a routerLink="/dashboard" routerLinkActive="text-sky-400" class="hover:text-sky-300">Dashboard</a>
      <a routerLink="/owners" routerLinkActive="text-sky-400" class="hover:text-sky-300">Propietarios</a>
      <a routerLink="/vehicles" routerLinkActive="text-sky-400" class="hover:text-sky-300">Vehículos</a>
      <a routerLink="/maintenance" routerLinkActive="text-sky-400" class="hover:text-sky-300">Mantenimientos</a>
    </nav>
    <main class="p-6 max-w-6xl mx-auto">
      <router-outlet></router-outlet>
    </main>
  `,
})
export class AppComponent {}
