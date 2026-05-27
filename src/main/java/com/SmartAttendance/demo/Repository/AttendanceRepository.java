package com.SmartAttendance.demo.Repository;

import com.SmartAttendance.demo.Entities.AttEnum;
import com.SmartAttendance.demo.Entities.Attendance;
import com.SmartAttendance.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByUserUserIdAndClassRoomClassId(Long userId, Long classId);

    List<Attendance> findByClassRoomClassId(Long classId);

    Optional<Attendance> findByUser_UserIdAndClassRoom_ClassIdAndDate(
            Long userId,
            Long classId,
            LocalDate date
    );

    List<Attendance> findByUser(User user);

    @Query("""
        SELECT COUNT(DISTINCT a.date)
        FROM Attendance a
        WHERE a.classRoom.classId = :classId
    """)
    long countTotalSessionsByClassId(@Param("classId") Long classId);

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.user.userId = :userId
        AND a.classRoom.classId = :classId
        AND a.isPresent = :status
    """)
    long countByUserIdAndClassIdAndIsPresent(
            @Param("userId") Long userId,
            @Param("classId") Long classId,
            @Param("status") AttEnum status
    );

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.classRoom.classId = :classId
        AND a.date = CURRENT_DATE
        AND a.isPresent = com.SmartAttendance.demo.Entities.AttEnum.PRESENT
    """)
    long countPresentToday(@Param("classId") Long classId);

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.classRoom.classId = :classId
        AND a.timestamp >= :cutoff
    """)
    long countSessionsInLastNDays(
            @Param("classId") Long classId,
            @Param("cutoff") LocalDateTime cutoff
    );

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.user.userId = :studentId
        AND a.classRoom.classId = :classId
        AND a.isPresent = :status
        AND a.timestamp >= :cutoff
    """)
    long countAttendedInLastNDays(
            @Param("studentId") Long studentId,
            @Param("classId") Long classId,
            @Param("status") AttEnum status,
            @Param("cutoff") LocalDateTime cutoff
    );

    @Query(value = """
    SELECT
        s.date,
        ROUND(
            SUM(SUM(CASE WHEN a.is_present = 0 THEN 1 ELSE 0 END)) OVER (ORDER BY s.date) * 100.0
            / SUM(COUNT(*)) OVER (ORDER BY s.date),
            2
        ) AS cumulative_pct
    FROM (
        SELECT DISTINCT date FROM attendance WHERE class_id = :classId
    ) s
    LEFT JOIN attendance a ON a.date = s.date 
        AND a.class_id = :classId 
        AND a.user_id = :studentId
    GROUP BY s.date
    ORDER BY s.date ASC
    LIMIT 14
""", nativeQuery = true)
    List<Object[]> getLast14DailyAttendancePercentages(
            @Param("studentId") Long studentId,
            @Param("classId") Long classId
    );
}
