import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Employee } from '../models/employee';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/employees';

  constructor(private http: HttpClient) {}

  loginByEmail(email: string): Observable<Employee | null> {
    return this.http.get<Employee[]>(this.apiUrl).pipe(
      map((employees) => {
        const found = employees.find((emp) => emp.email === email);
        return found || null;
      }),
    );
  }

  setCurrentUser(user: Employee): void {
    localStorage.setItem('currentUser', JSON.stringify(user));
  }

  getCurrentUser(): Employee | null {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user) : null;
  }

  isLoggedIn(): boolean {
    return this.getCurrentUser() !== null;
  }

  logout(): void {
    localStorage.removeItem('currentUser');
  }
}
