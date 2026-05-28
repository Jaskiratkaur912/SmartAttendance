package com.SmartAttendance.demo.Consumer;

import com.SmartAttendance.demo.KafkaEvent.AssignmentPostedEvent;
import com.SmartAttendance.demo.Repository.ClassRepository;
import com.SmartAttendance.demo.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AssignmentAlertConsumer {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private NotificationService notificationService;
    @KafkaListener(topics = "assignment.posted",
            groupId = "assignment-alert")
    public void consume(AssignmentPostedEvent assignmentPostedEvent){
        //we need to send an in app notif to all the students enrolled in the class
        Long classId= assignmentPostedEvent.getClassId();
        List<Long> enrolledStudents=classRepository.findEnrolledStudentIdsByClassId(classId);
        LocalDateTime deadline=assignmentPostedEvent.getDeadline();
        for(Long studId:enrolledStudents){
            //we need to send notif to this student now
            String message = String.format(
                    "📚 New Assignment Posted! Deadline: %s",
                    deadline.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
            );
        }
    }
}
