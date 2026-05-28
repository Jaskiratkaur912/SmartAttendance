package com.SmartAttendance.demo.KafkaEvent;

import com.SmartAttendance.demo.Entities.Assignment;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssignmentPostedEvent {
    private Long classId;
    private LocalDateTime deadline;
    public AssignmentPostedEvent(Long classId, LocalDateTime deadline){
        this.classId=classId;
        this.deadline=deadline;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Long getClassId() {
        return classId;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
