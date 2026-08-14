package com.drx.leave_manager.controller;

import com.drx.leave_manager.model.LeaveRequest;
import com.drx.leave_manager.service.LeaveRequestService;
import com.drx.leave_manager.service.PdfExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final PdfExportService pdfExportService;

    @GetMapping
    public List<LeaveRequest> getAllRequests() {
        return leaveRequestService.getAllRequests();
    }

    @GetMapping("/{id}")
    public LeaveRequest getRequestById(@PathVariable Integer id) {
        return leaveRequestService.getRequestById(id);
    }

    @PostMapping
    public LeaveRequest createLeaveRequest(@RequestBody LeaveRequest request) {
        return leaveRequestService.createLeaveRequest(request);
    }

    @GetMapping("/search")
    public List<LeaveRequest> filterRequests(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer leaveTypeId,
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return leaveRequestService.filterRequests(status, departmentId, leaveTypeId, employeeId, startDate, endDate);
    }

    @PutMapping("/{id}/status")
    public LeaveRequest updateStatus(
            @PathVariable Integer id,
            @RequestParam String status,
            @RequestParam Integer reviewerId,
            @RequestParam(required = false) String comment) {
        return leaveRequestService.updateRequestStatus(id, status, reviewerId, comment);
    }

    @GetMapping("/{id}/pdf")
    public void exportToPdf(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=cerere_concediu_" + id + ".pdf";
        response.setHeader(headerKey, headerValue);

        LeaveRequest request = leaveRequestService.getRequestById(id);
        pdfExportService.exportLeaveRequestToPdf(request, response);
    }
}