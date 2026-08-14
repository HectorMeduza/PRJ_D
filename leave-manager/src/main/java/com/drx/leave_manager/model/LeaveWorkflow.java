package com.drx.leave_manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "leave_workflow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workflow_id")
    private Integer workflowId;

    @ManyToOne
    @JoinColumn(name = "leave_request_id")
    private LeaveRequest leaveRequest;

    @ManyToOne
    @JoinColumn(name = "empl_id")
    private Employee employee;

    @Column(name = "old_status")
    private String oldStatus;

    @Column(name = "current_status")
    private String currentStatus;

    private String comment;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}