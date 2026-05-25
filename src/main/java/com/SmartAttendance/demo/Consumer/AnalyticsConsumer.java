package com.SmartAttendance.demo.Consumer;

import com.SmartAttendance.demo.KafkaEvent.SessionClosedEvent;
import com.SmartAttendance.demo.Repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnalyticsConsumer {
    @Autowired
    private ClassRepository classRepository;
    @KafkaListener(topics = "session.closed",
            groupId = "analytics-service")
    public void consume(SessionClosedEvent sessionClosedEvent){
        //this consumer basically loops over all the students to provide them insights on their attendance trends
        Long classId=sessionClosedEvent.getClassId();
        List<Long> enrolledStudents=classRepository.findEnrolledStudentIdsByClassId(classId);

    }

}
