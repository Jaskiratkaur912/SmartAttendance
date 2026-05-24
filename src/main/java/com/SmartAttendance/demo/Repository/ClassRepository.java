package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.ClassRoom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassRoom,Long>{
    // ClassRepository.java
    @Query("SELECT u.id FROM ClassRoom c JOIN c.enrolledStudents u WHERE c.id = :classId")
    List<Long> findEnrolledStudentIdsByClassId(@Param("classId") Long classId);
    boolean existsByClassCode(String code);
    List<ClassRoom> findByTeacherId(Long teacherId);
    Optional<ClassRoom> findByClassCode(String code);
}
