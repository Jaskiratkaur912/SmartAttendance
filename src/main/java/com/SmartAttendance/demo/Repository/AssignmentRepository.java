package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.Assign;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment,Long> {

    Optional<Assignment> fetchById(Long assignmentId);
}
