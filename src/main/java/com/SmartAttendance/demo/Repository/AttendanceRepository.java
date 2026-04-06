package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findByUserUserIdAndClassRoomClassId(Long userId,Long classId);
    List<Attendance> findByClassRoomClassId(Long classId);
    Optional<Attendance> findByUser_UserIdAndClassRoom_ClassIdAndDate(Long userId, Long classId, LocalDate date);
}

