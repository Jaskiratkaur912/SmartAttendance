package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.Assignment;
import com.SmartAttendance.demo.Entities.AssignmentSubmission;
import com.SmartAttendance.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    // 🔥 get all submissions for an assignment (teacher use)
    List<AssignmentSubmission> findByAssignment_Id(Long id);

    // 🔥 check if student already submitted
    List<AssignmentSubmission> findByAssignmentAndStudent(Assignment assignment, User student);
}