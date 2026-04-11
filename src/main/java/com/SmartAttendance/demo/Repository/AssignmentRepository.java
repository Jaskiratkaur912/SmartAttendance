package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment,Long> {

}
