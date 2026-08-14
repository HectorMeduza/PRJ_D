package com.drx.leave_manager.service;

import com.drx.leave_manager.model.Employee;
import com.drx.leave_manager.model.LeaveRequest;
import com.drx.leave_manager.model.LeaveWorkflow;
import com.drx.leave_manager.repository.EmployeeRepository;
import com.drx.leave_manager.repository.LeaveRequestRepository;
import com.drx.leave_manager.repository.LeaveWorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveWorkflowRepository leaveWorkflowRepository;
    private final EmployeeRepository employeeRepository;

    public List<LeaveRequest> getAllRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest getRequestById(Integer id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cererea cu ID-ul " + id + " nu a fost găsită."));
    }

    private boolean isPublicHoliday(LocalDate date) {
        int year = date.getYear();
        List<LocalDate> holidays = List.of(
                LocalDate.of(year, 1, 1),   // Anul Nou
                LocalDate.of(year, 1, 2),   // Anul Nou
                LocalDate.of(year, 1, 24),  // Unirea Principatelor
                LocalDate.of(year, 5, 1),   // Ziua Muncii
                LocalDate.of(year, 6, 1),   // Ziua Copilului
                LocalDate.of(year, 8, 15),  // Adormirea Maicii Domnului
                LocalDate.of(year, 11, 30), // Sfântul Andrei
                LocalDate.of(year, 12, 1),  // Ziua Națională
                LocalDate.of(year, 12, 25), // Crăciun
                LocalDate.of(year, 12, 26)  // Crăciun
        );
        return holidays.contains(date);
    }

    public int calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        int workingDays = 0;
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            DayOfWeek day = current.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY && !isPublicHoliday(current)) {
                workingDays++;
            }
            current = current.plusDays(1);
        }
        return workingDays;
    }

    @Transactional
    public LeaveRequest createLeaveRequest(LeaveRequest request) {
        int workingDays = calculateWorkingDays(request.getStartDate(), request.getEndDate());
        request.setWorkingDays(workingDays);

        Employee employee = employeeRepository.findById(request.getEmployee().getEmplId())
                .orElseThrow(() -> new RuntimeException("Angajatul nu există."));

        Integer maxAbsent = employee.getDepartment().getMaxAbsentEmployees();

        List<LeaveRequest> overlappingRequests = leaveRequestRepository.filterRequests(
                "APPROVED", employee.getDepartment().getDeptId(), null, null, request.getStartDate(), request.getEndDate()
        );

        if (maxAbsent != null && overlappingRequests.size() >= maxAbsent) {
            throw new RuntimeException("Cererea nu poate fi depusă: s-a atins numărul maxim de angajați absenți simultan în departament (" + maxAbsent + ").");
        }

        if (request.getStatus() == null) {
            request.setStatus("PENDING");
        }
        request.setCreatedAt(LocalDate.now());

        LeaveRequest savedRequest = leaveRequestRepository.save(request);

        recordWorkflowHistory(savedRequest, employee, null, savedRequest.getStatus(), "Cerere creată în sistem.");

        return savedRequest;
    }

    @Transactional
    public LeaveRequest updateRequestStatus(Integer requestId, String newStatus, Integer reviewerId, String comment) {
        LeaveRequest request = getRequestById(requestId);
        Employee reviewer = employeeRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Aprobatorul nu a fost găsit."));

        String oldStatus = request.getStatus();

        if ("REJECTED".equalsIgnoreCase(newStatus) && (comment == null || comment.trim().isEmpty())) {
            throw new RuntimeException("Comentariul este obligatoriu în cazul respingerii unei cereri!");
        }

        if ("APPROVED".equalsIgnoreCase(newStatus) && !"APPROVED".equalsIgnoreCase(oldStatus)) {
            Employee applicant = request.getEmployee();
            if (applicant.getAvailableLeaveDays() < request.getWorkingDays()) {
                throw new RuntimeException("Angajatul nu are suficiente zile libere disponibile (" + applicant.getAvailableLeaveDays() + " rămase).");
            }
            applicant.setAvailableLeaveDays(applicant.getAvailableLeaveDays() - request.getWorkingDays());
            employeeRepository.save(applicant);
        }

        request.setStatus(newStatus.toUpperCase());
        LeaveRequest updatedRequest = leaveRequestRepository.save(request);

        recordWorkflowHistory(updatedRequest, reviewer, oldStatus, updatedRequest.getStatus(), comment);

        return updatedRequest;
    }

    public List<LeaveRequest> filterRequests(String status, Integer departmentId, Integer leaveTypeId,
                                             Integer employeeId, LocalDate startDate, LocalDate endDate) {
        return leaveRequestRepository.filterRequests(status, departmentId, leaveTypeId, employeeId, startDate, endDate);
    }

    private void recordWorkflowHistory(LeaveRequest request, Employee employee, String oldStatus, String currentStatus, String comment) {
        LeaveWorkflow workflow = new LeaveWorkflow();
        workflow.setLeaveRequest(request);
        workflow.setEmployee(employee);
        workflow.setOldStatus(oldStatus);
        workflow.setCurrentStatus(currentStatus);
        workflow.setComment(comment);
        workflow.setChangedAt(LocalDateTime.now());

        leaveWorkflowRepository.save(workflow);
    }
}