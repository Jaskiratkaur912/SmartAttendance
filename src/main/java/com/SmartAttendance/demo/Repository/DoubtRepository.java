package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.Doubt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DoubtRepository extends MongoRepository<Doubt,String> {

    List<Doubt> findByAssignmentId(Long assignmentId);
}
