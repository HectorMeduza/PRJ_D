package com.drx.leave_manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empl_id")
    private Integer emplId;

    private String name;
    private String email;
    private String role;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;

    @Column(name = "annual_leave_days")
    private Integer annualLeaveDays;

    @Column(name = "available_leave_days")
    private Integer availableLeaveDays;
}