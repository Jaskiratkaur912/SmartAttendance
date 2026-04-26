package com.SmartAttendance.demo.Controller;

import com.SmartAttendance.demo.DTO.ResolveDoubt;
import com.SmartAttendance.demo.Entities.Doubt;
import com.SmartAttendance.demo.Entities.DoubtStatus;
import com.SmartAttendance.demo.Repository.DoubtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class DoubtWebSocketController {
    @Autowired
    DoubtRepository doubtRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @MessageMapping("askDoubt/{assignmentId}")
    @SendTo("/topic/doubts/{assignmentId}")
    public Doubt handleDoubt(@DestinationVariable Long assignmentId,
                             Doubt message){
        System.out.println("Received doubt: " + message.getQuestion() + " | studentId: " + message.getStudentId());
        message.setAssignmentId(assignmentId);
        message.setCreatedAt(LocalDateTime.now());
        message.setStatus(DoubtStatus.OPEN);
        doubtRepository.save(message);
        return message;
    }
    @MessageMapping("resolveDoubt/{doubtId}")
    public Doubt resolveDoubt(@DestinationVariable String doubtId, ResolveDoubt resolution){
        Doubt doubt=doubtRepository.findById(doubtId).orElseThrow(()->new RuntimeException("no doubt found"));
        doubt.setResolution(resolution.getResolution());
        doubt.setStatus(DoubtStatus.CLOSED);
        doubtRepository.save(doubt);
        messagingTemplate.convertAndSend("/topic/doubts/" + doubt.getAssignmentId(), doubt);
        return doubt;
    }


    
}
