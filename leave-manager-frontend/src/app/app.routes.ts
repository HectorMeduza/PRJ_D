import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { DashboardComponent } from './components/dashboard/dashboard';
import { LeaveRequestListComponent } from './components/leave-request-list/leave-request-list';
import { LeaveRequestFormComponent } from './components/leave-request-form/leave-request-form';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'requests', component: LeaveRequestListComponent, canActivate: [authGuard] },
  { path: 'new-request', component: LeaveRequestFormComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: 'dashboard' },
];
