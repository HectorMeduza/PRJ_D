import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class LoginComponent {
  email: string = '';
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  onLogin(): void {
    if (!this.email) {
      this.errorMessage = 'Te rog introdu o adresă de email.';
      return;
    }

    this.authService.loginByEmail(this.email).subscribe({
      next: (user) => {
        if (!user) {
          this.errorMessage = 'Utilizatorul cu acest email nu a fost găsit în sistem.';
          return;
        }

        this.authService.setCurrentUser(user);

        if (user.role === 'ADMIN' || this.email === 'admin@gmail.com') {
          void this.router.navigate(['/admin-dashboard']); // Interfața de manager
        } else {
          void this.router.navigate(['/requests']); // Interfața de cereri (angajat)
        }
      },
      error: () => {
        this.errorMessage = 'Eroare de comunicare cu serverul de backend.';
      },
    });
  }
}
