package com.SmartAttendance.demo.Consumer;

import com.SmartAttendance.demo.Entities.AttEnum;
import com.SmartAttendance.demo.Entities.TrendEnum;
import com.SmartAttendance.demo.KafkaEvent.SessionClosedEvent;
import com.SmartAttendance.demo.Repository.AttendanceRepository;
import com.SmartAttendance.demo.Repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Math.floor;

@Component
public class AnalyticsConsumer {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @KafkaListener(topics = "session.closed",
            groupId = "analytics-service")

    public void consume(SessionClosedEvent sessionClosedEvent){
        LocalDateTime cutOff = LocalDateTime.now().minusDays(14);
        //this consumer basically loops over all the students to provide them insights on their attendance trends
        Long classId=sessionClosedEvent.getClassId();
        List<Long> enrolledStudents=classRepository.findEnrolledStudentIdsByClassId(classId);
        for(Long studId:enrolledStudents){
            //for this student we need to give insights such as:
            // the number of classes that he/she can miss
            long attended=attendanceRepository.countByUserIdAndClassIdAndIsPresent(studId,classId, AttEnum.PRESENT);
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
            //wrapping al this data in AnalyticsDTO


        }
    }

}
