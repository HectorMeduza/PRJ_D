import { Employee } from './employee';

export interface LeaveRequest {
  id: number;
  startDate: string;
  endDate: string;
  reason: string;
  status: string;
  employee: Employee;
}
