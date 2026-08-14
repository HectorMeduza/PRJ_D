import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LeaveRequestService } from '../../services/leave-request';
import { LeaveRequest } from '../../models/leave-request';
import { AuthService } from '../../services/auth';
import { Employee } from '../../models/employee';

@Component({
  selector: 'app-leave-request-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './leave-request-list.html',
  styleUrl: './leave-request-list.scss',
})
export class LeaveRequestListComponent implements OnInit {
  requests: LeaveRequest[] = [];
  filteredRequests: any[] = [];
  currentUser: Employee | null = null;

  selectedStatus: string = '';
  loading: boolean = false;
  errorMessage: string = '';

  constructor(
    private leaveRequestService: LeaveRequestService,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.loadRequests();
  }

  loadRequests(): void {
    this.loading = true;
    this.leaveRequestService.getAllRequests().subscribe({
      next: (data) => {
        if (this.currentUser?.role === 'ADMIN') {
          this.requests = data;
        } else if (this.currentUser) {
          this.requests = data.filter((r) => r.employee?.id === this.currentUser?.id);
        }
        this.filteredRequests = [...this.requests];
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Eroare la preluarea cererilor de concediu.';
        this.loading = false;
      },
    });
  }

  filterByStatus(): void {
    if (!this.selectedStatus) {
      this.filteredRequests = [...this.requests];
    } else {
      this.filteredRequests = this.requests.filter((r) => r.status === this.selectedStatus);
    }
  }

  cancelRequest(id: number): void {
    console.log('Anulare cerere ID:', id);
  }

  downloadPdf(id: number): void {
    console.log('Descărcare PDF pentru cererea ID:', id);
  }
}
