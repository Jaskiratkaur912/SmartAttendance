package com.SmartAttendance.demo.Service;

import com.SmartAttendance.demo.Entities.AttEnum;
import com.SmartAttendance.demo.Entities.SubjectAnalytics;
import com.SmartAttendance.demo.Entities.SubjectAnalyticsId;
import com.SmartAttendance.demo.Entities.TrendEnum;
import com.SmartAttendance.demo.Repository.AttendanceRepository;
import com.SmartAttendance.demo.Repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Math.floor;
@Service
public class AnalyticService {
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    ClassRepository classRepository;
    public SubjectAnalytics buildAnalytics(Long studId, Long classId){
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
        List<Double> DailyPcts =
                attendanceRepository
                        .getLast14DailyAttendancePercentages(
                                studId,
                                classId
                        );
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
        subjectAnalyticsDTO.setDailyPcts(DailyPcts);
        subjectAnalyticsDTO.setRecentPct(recentPct);
        subjectAnalyticsDTO.setComputedAt(LocalDateTime.now());
        return subjectAnalyticsDTO;

    }
}
