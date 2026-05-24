package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.AttEnum;
import com.SmartAttendance.demo.Entities.Attendance;
import com.SmartAttendance.demo.Entities.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findByUserUserIdAndClassRoomClassId(Long userId,Long classId);
    List<Attendance> findByClassRoomClassId(Long classId);
    Optional<Attendance> findByUser_UserIdAndClassRoom_ClassIdAndDate(Long userId, Long classId, LocalDate date);
    List<Attendance> findByUser(User user);

    @Query("SELECT COUNT(DISTINCT a.date) FROM Attendance a WHERE a.classRoom.classId = :classId")
    long countTotalSessionsByClassId(@Param("classId") Long classId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.user.id = :userId AND a.classRoom.classId = :classId AND a.isPresent = :status")
    long countByUserIdAndClassIdAndIsPresent(@Param("userId") Long userId, @Param("classId") Long classId, @Param("status") AttEnum status);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.classRoom.classId = :classId AND a.date = CURRENT_DATE AND a.isPresent = com.SmartAttendance.demo.Entities.AttEnum.PRESENT")
    long countPresentToday(@Param("classId") Long classId);
}

