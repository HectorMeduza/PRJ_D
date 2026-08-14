package com.drx.leave_manager.service;

import com.drx.leave_manager.model.LeaveType;
import com.drx.leave_manager.repository.LeaveTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }

    public LeaveType getLeaveTypeById(Integer id) {
        return leaveTypeRepository.findById(id).orElse(null);
    }

    public LeaveType saveLeaveType(LeaveType leaveType) {
        return leaveTypeRepository.save(leaveType);
    }

    public void deleteLeaveType(Integer id) {
        leaveTypeRepository.deleteById(id);
    }
}