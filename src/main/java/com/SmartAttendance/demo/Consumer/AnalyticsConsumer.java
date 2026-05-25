package com.SmartAttendance.demo.Consumer;

import com.SmartAttendance.demo.Entities.AttEnum;
import com.SmartAttendance.demo.KafkaEvent.SessionClosedEvent;
import com.SmartAttendance.demo.Repository.AttendanceRepository;
import com.SmartAttendance.demo.Repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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
        //this consumer basically loops over all the students to provide them insights on their attendance trends
        Long classId=sessionClosedEvent.getClassId();
        List<Long> enrolledStudents=classRepository.findEnrolledStudentIdsByClassId(classId);
        for(Long studId:enrolledStudents){
            //for this student we need to give insights such as:
            // the number of classes that he/she can miss
            long attended=attendanceRepository.countByUserIdAndClassIdAndIsPresent(studId,classId, AttEnum.PRESENT);
            long total=attendanceRepository.countTotalSessionsByClassId(classId);
            long missable = (long)floor((attended - 0.75 * total) / 0.25);
        }
    }

}
