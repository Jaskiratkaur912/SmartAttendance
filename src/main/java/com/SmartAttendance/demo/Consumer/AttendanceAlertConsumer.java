package com.SmartAttendance.demo.Consumer;

import com.SmartAttendance.demo.KafkaEvent.AlertEvent;
import com.SmartAttendance.demo.KafkaEvent.NotificationEvent;
import com.SmartAttendance.demo.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AttendanceAlertConsumer {
    @Autowired
    private NotificationService notificationService;
    @KafkaListener(
            topics="attendance.alert",
            groupId="notification-service"
    )
    public void consume(AlertEvent alertEvent){
        String message = String.format(
                "⚠️ Low attendance alert! Your attendance in class %d is %.1f%%. " +
                        "Minimum required is 75%%.",
                alertEvent.getClassId(),
                alertEvent.getRate()
        );
        NotificationEvent notification = new NotificationEvent(
                alertEvent.getStudentId(),
                message,
                "ATTENDANCE_ALERT"
        );

        notificationService.sendInAppNotification(notification);
    }
}
