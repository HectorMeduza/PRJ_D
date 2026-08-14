import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LeaveRequestService } from '../../services/leave-request';
import { AuthService } from '../../services/auth';
import { LeaveRequest } from '../../models/leave-request';
import { Employee } from '../../models/employee';

@Component({
  selector: 'app-leave-request-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './leave-request-form.html',
  styleUrl: './leave-request-form.scss',
})
export class LeaveRequestFormComponent implements OnInit {
  currentUser: Employee | null = null;

  selectedTypeId: number = 0;
  leaveTypes: any[] = [
    { leave_type_id: 1, name: 'Concediu de odihnă' },
    { leave_type_id: 2, name: 'Concediu medical' },
    { leave_type_id: 3, name: 'Concediu fără plată' },
  ];
  startDate: string = '';
  endDate: string = '';
  requiresAttachment: boolean = false;
  selectedFile: File | null = null;
  errorMessage: string = '';

  request: LeaveRequest = {
    id: 0,
    startDate: '',
    endDate: '',
    reason: '',
    status: 'PENDING',
    employee: {} as Employee,
  };

  constructor(
    private leaveRequestService: LeaveRequestService,
    private authService: AuthService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
  }

  onTypeChange(): void {
    this.requiresAttachment = this.selectedTypeId === 2;
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  onSubmit(): void {
    if (!this.currentUser) {
      this.errorMessage = 'Utilizatorul nu este autentificat.';
      return;
    }

    this.request.startDate = this.startDate;
    this.request.endDate = this.endDate;
    this.request.employee = this.currentUser;

    this.leaveRequestService.createRequest(this.request).subscribe({
      next: () => {
        void this.router.navigate(['/requests']);
      },
      error: () => {
        this.errorMessage = 'Eroare la crearea cererii de concediu.';
      },
    });
  }
}
