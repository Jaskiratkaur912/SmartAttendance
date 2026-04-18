package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassRoom,Long>{

    boolean existsByClassCode(String code);
    List<ClassRoom> findByTeacherId(Long teacherId);
    Optional<ClassRoom> findByClassCode(String code);
}
