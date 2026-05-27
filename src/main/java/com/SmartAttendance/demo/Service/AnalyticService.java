package com.SmartAttendance.demo.Service;

import com.SmartAttendance.demo.Entities.*;
import com.SmartAttendance.demo.Repository.AttendanceRepository;
import com.SmartAttendance.demo.Repository.ClassRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.floor;
@Service
public class AnalyticService {
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    ClassRepository classRepository;
    public SubjectAnalytics buildAnalytics(Long studId, Long classId) throws JsonProcessingException {
        LocalDateTime cutOff = LocalDateTime.now().minusDays(14);
        Long attended=attendanceRepository.countByUserIdAndClassIdAndIsPresent(studId,classId, AttEnum.PRESENT);
        long total=attendanceRepository.countTotalSessionsByClassId(classId);
        long missable = (long)floor((attended - 0.75 * total) / 0.25);
        double attendancePct = (double) attended / total * 100;
        //adding velocity analytics
        long recentTotal = attendanceRepository.countSessionsInLastNDays(classId, cutOff);
        long recentAttended = attendanceRepository.countAttendedInLastNDays(studId, classId, AttEnum.PRESENT,cutOff);
        double recentPct = (double) recentAttended / recentTotal * 100;
        double overallPct = attendancePct;
        double velocity = recentPct - overallPct;
        TrendEnum trend = velocity > 3 ? TrendEnum.IMPROVING
                : velocity < -3 ? TrendEnum.SLIPPING
                : TrendEnum.STABLE;
        List<DailyAnalyticsPts> dailyPcts = attendanceRepository
                .getLast14DailyAttendancePercentages(studId, classId)
                .stream()
                .map(row -> new DailyAnalyticsPts(
                        ((java.sql.Date) row[0]).toLocalDate(),
                        row[1] != null ? ((Number) row[1]).doubleValue() : null
                ))
                .collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String dailyPctsJson = mapper.writeValueAsString(dailyPcts);
        String className = classRepository
                .findById(classId)
                .orElseThrow()
                .getClassName();
        SubjectAnalyticsId compositeKey = new SubjectAnalyticsId();
        compositeKey.setStudentId(studId);
        compositeKey.setClassId(classId);
        SubjectAnalytics subjectAnalyticsDTO=new SubjectAnalytics();
        subjectAnalyticsDTO.setAttended(attended);
        subjectAnalyticsDTO.setId(compositeKey);
        subjectAnalyticsDTO.setClassName(className);
        subjectAnalyticsDTO.setMissable(missable);
        subjectAnalyticsDTO.setTotal(total);
        subjectAnalyticsDTO.setTrend(trend);
        subjectAnalyticsDTO.setVelocity(velocity);
        subjectAnalyticsDTO.setAttendancePct(attendancePct);
        subjectAnalyticsDTO.setDailyPcts(dailyPctsJson);
        subjectAnalyticsDTO.setRecentPct(recentPct);
        subjectAnalyticsDTO.setComputedAt(LocalDateTime.now());
        return subjectAnalyticsDTO;

    }
}
