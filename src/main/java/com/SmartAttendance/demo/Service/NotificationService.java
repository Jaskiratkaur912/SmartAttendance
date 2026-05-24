package com.SmartAttendance.demo.Service;

import com.SmartAttendance.demo.KafkaEvent.HeadCntEvent;
import com.SmartAttendance.demo.KafkaEvent.NotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    public void sendInAppNotification(NotificationEvent notification) {
        messagingTemplate.convertAndSend(
                "/topic/notification/" + notification.getStudentId(),
                notification
        );
    }
    public void sendInAppHeadCntNotif(HeadCntEvent headCntEvent){
        messagingTemplate.convertAndSend("/topic/teacher/" + headCntEvent.getClassId(),headCntEvent);
    }
}