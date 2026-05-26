package com.SmartAttendance.demo.Consumer;



import com.SmartAttendance.demo.Entities.AttEnum;
import com.SmartAttendance.demo.Entities.SubjectAnalytics;
import com.SmartAttendance.demo.Entities.TrendEnum;
import com.SmartAttendance.demo.KafkaEvent.SessionClosedEvent;
import com.SmartAttendance.demo.Repository.AttendanceRepository;
import com.SmartAttendance.demo.Repository.ClassRepository;
import com.SmartAttendance.demo.Repository.SubjectAnalyticsRepository;
import com.SmartAttendance.demo.Service.AnalyticService;
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
    private AnalyticService analyticService;
    @Autowired
    private SubjectAnalyticsRepository subjectAnalyticsRepository;
    @KafkaListener(topics = "session.closed",
            groupId = "analytics-service")

    public void consume(SessionClosedEvent sessionClosedEvent){
        System.out.println("Kafka event received for classId=" + sessionClosedEvent.getClassId());
        LocalDateTime cutOff = LocalDateTime.now().minusDays(14);
        //this consumer basically loops over all the students to provide them insights on their attendance trends
        Long classId=sessionClosedEvent.getClassId();
        List<Long> enrolledStudents=classRepository.findEnrolledStudentIdsByClassId(classId);
        System.out.println("Enrolled students count: " + enrolledStudents.size());
        for(Long studId:enrolledStudents){
            System.out.println("Building analytics for studentId=" + studId);
            //for this student we need to give insights such as:
            // the number of classes that he/she can miss
            try {
                SubjectAnalytics dto = analyticService.buildAnalytics(studId, classId);
                subjectAnalyticsRepository.save(dto);
                System.out.println("Saved analytics for studentId=" + studId);

            } catch (Exception e) {
                System.out.println("FAILED for studentId=" + studId);
                e.printStackTrace();
            }
            // we now have to persist this dto

        }
    }

}
