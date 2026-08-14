package com.drx.leave_manager.controller;

import com.drx.leave_manager.model.LeaveType;
import com.drx.leave_manager.service.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-types")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @GetMapping
    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeService.getAllLeaveTypes();
    }

    @GetMapping("/{id}")
    public LeaveType getLeaveTypeById(@PathVariable Integer id) {
        return leaveTypeService.getLeaveTypeById(id);
    }

    @PostMapping
    public LeaveType createLeaveType(@RequestBody LeaveType leaveType) {
        return leaveTypeService.saveLeaveType(leaveType);
    }

    @DeleteMapping("/{id}")
    public void deleteLeaveType(@PathVariable Integer id) {
        leaveTypeService.deleteLeaveType(id);
    }
}