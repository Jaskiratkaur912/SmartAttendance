package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.Doubt;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoubtRepository extends MongoRepository<Doubt,String> {

}
