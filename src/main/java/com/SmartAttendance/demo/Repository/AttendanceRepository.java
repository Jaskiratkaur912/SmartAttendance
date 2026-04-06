package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findByUserUserIdAndClassRoomClassId(Long userId,Long classId);
    List<Attendance> findByClassRoomClassId(Long classId);
}

