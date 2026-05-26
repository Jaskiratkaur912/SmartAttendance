package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.AttEnum;
import com.SmartAttendance.demo.Entities.Attendance;
import com.SmartAttendance.demo.Entities.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Query("SELECT COUNT(a) FROM Attendance a " +
            "WHERE a.classId = :classId " +
            "AND a.createdAt >= :cutoff")
    long countSessionsInLastNDays(
            @Param("classId") Long classId,
            @Param("cutoff") LocalDateTime cutoff
    );

    @Query("SELECT COUNT(a) FROM Attendance a " +
            "WHERE a.userId = :studId " +
            "AND a.classId = :classId " +
            "AND a.status = :status " +
            "AND a.createdAt >= :cutoff")
    long countAttendedInLastNDays(
            @Param("studId") Long studId,
            @Param("classId") Long classId,
            @Param("status") AttEnum status,
            @Param("cutoff") LocalDateTime cutoff
    );
    @Query(value = """
    SELECT 
        ROUND(
            (
                SUM(
                    CASE 
                        WHEN is_present = 'PRESENT'
                        THEN 1 
                        ELSE 0 
                    END
                ) * 100.0
            ) / COUNT(*),
            2
        ) AS attendance_percentage

    FROM attendance

    WHERE user_id = :studentId
      AND class_id = :classId

    GROUP BY attendance_date

    ORDER BY attendance_date DESC

    LIMIT 8
    """, nativeQuery = true)
    List<Double> getLast8AttendancePercentages(
            @Param("studentId") Long studentId,
            @Param("classId") Long classId
    );
}

