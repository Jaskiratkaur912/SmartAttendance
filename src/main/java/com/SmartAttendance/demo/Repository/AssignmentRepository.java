package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.Assign;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment,Long> {

    List<Assignment> findByClassId(Long id);
}
