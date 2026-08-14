import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LeaveRequestService } from '../../services/leave-request';
import { LeaveRequest } from '../../models/leave-request';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class DashboardComponent implements OnInit {
  totalRequests: number = 0;
  pendingRequests: number = 0;
  approvedRequests: number = 0;
  rejectedRequests: number = 0;
  loading: boolean = true;

  constructor(private leaveRequestService: LeaveRequestService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.leaveRequestService.getAllRequests().subscribe({
      next: (data: LeaveRequest[]) => {
        this.totalRequests = data.length;
        this.pendingRequests = data.filter((r) => r.status === 'PENDING').length;
        this.approvedRequests = data.filter((r) => r.status === 'APPROVED').length;
        this.rejectedRequests = data.filter((r) => r.status === 'REJECTED').length;
        this.loading = false;
      },
      error: (err) => {
        console.error('Eroare la încărcarea datelor pentru dashboard:', err);
        this.loading = false;
      },
    });
  }
}
