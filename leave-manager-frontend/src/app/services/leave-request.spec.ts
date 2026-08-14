import { TestBed } from '@angular/core/testing';
import { LeaveRequestService } from './leave-request';
import { provideHttpClient } from '@angular/common/http';

describe('LeaveRequestService', () => {
  let service: LeaveRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient()],
    });
    service = TestBed.inject(LeaveRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
