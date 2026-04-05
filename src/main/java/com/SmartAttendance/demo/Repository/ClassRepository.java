package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import javax.swing.text.html.Option;
@Repository
public interface ClassRepository extends JpaRepository<ClassRoom,Long>{

    boolean existsByClassCode(String code);

    Optional<ClassRoom> findByClassCode(String code);
}
