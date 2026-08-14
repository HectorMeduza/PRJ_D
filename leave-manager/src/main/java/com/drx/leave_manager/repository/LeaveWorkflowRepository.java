package com.drx.leave_manager.repository;

import com.drx.leave_manager.model.LeaveWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveWorkflowRepository extends JpaRepository<LeaveWorkflow, Integer> {
}