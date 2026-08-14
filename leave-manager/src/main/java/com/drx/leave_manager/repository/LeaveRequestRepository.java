package com.drx.leave_manager.repository;

import com.drx.leave_manager.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    @Query("SELECT r FROM LeaveRequest r WHERE " +
            "(:status IS NULL OR r.status = :status) AND " +
            "(:departmentId IS NULL OR r.employee.department.deptId = :departmentId) AND " +
            "(:leaveTypeId IS NULL OR r.leaveType.leaveTypeId = :leaveTypeId) AND " +
            "(:employeeId IS NULL OR r.employee.emplId = :employeeId) AND " +
            "(:startDate IS NULL OR r.endDate >= :startDate) AND " +
            "(:endDate IS NULL OR r.startDate <= :endDate)")
    List<LeaveRequest> filterRequests(
            @Param("status") String status,
            @Param("departmentId") Integer departmentId,
            @Param("leaveTypeId") Integer leaveTypeId,
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}