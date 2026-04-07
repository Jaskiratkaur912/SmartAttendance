package com.SmartAttendance.demo.Controller;

import com.SmartAttendance.demo.Entities.Doubt;
import com.SmartAttendance.demo.Repository.DoubtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class DoubtWebSocketController {
    @Autowired
    DoubtRepository doubtRepository;
    @MessageMapping("askDoubt/{assignmentId}")
    @SendTo("topic/doubts/{assignmentId}")
    public Doubt handleDoubt(@DestinationVariable Long assignmentId,
                             Doubt message){
        message.setAssignmentId(assignmentId);
        message.setCreatedAt(LocalDateTime.now());
        doubtRepository.save(message);
        return message;
    }

}
