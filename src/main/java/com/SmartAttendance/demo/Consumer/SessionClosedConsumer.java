package com.SmartAttendance.demo.Consumer;

import com.SmartAttendance.demo.Entities.ClassRoom;
import com.SmartAttendance.demo.Entities.User;
import com.SmartAttendance.demo.KafkaEvent.AlertEvent;
import com.SmartAttendance.demo.KafkaEvent.HeadCntEvent;
import com.SmartAttendance.demo.KafkaEvent.SessionClosedEvent;
import com.SmartAttendance.demo.Repository.ClassRepository;
import com.SmartAttendance.demo.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionClosedConsumer {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private KafkaTemplate<String, AlertEvent> kafkaTemplate;
    @Autowired
    private NotificationService notificationService;
    @KafkaListener(topics = "session.closed", groupId = "session-closed-group")
    public void consume(SessionClosedEvent sessionClosedEvent){
        Long classId=sessionClosedEvent.getClassId();
        // now we need to get all the students enrolled in this class
        ClassRoom classRoom=classRepository.findById(classId).orElse(null);
        if (classRoom == null) {
            System.out.println("ClassRoom not found for classId=" + classId);
            return;
        }
        List<Long> studentIds = classRepository.findEnrolledStudentIdsByClassId(classId);
        long headCnt=0L;
        for (Long studentId : studentIds) {
            String key        = "att:" + studentId + ":" + classId;
            String sessionKey = "session:" + classId + ":present:" + studentId;
            String alertKey   = "alerted:" + studentId + ":" + classId;

            // Always increment total for this session
            redisTemplate.opsForHash().increment(key, "total", 1);

            // Only increment present if they marked attendance THIS session
            if (Boolean.TRUE.equals(redisTemplate.hasKey(sessionKey))) {
                //we can increment the headCount for the session
                headCnt+=1;
                redisTemplate.opsForHash().increment(key, "present", 1);
                redisTemplate.delete(sessionKey); // cleanup
            }

            String totalStr   = (String) redisTemplate.opsForHash().get(key, "total");
            String presentStr = (String) redisTemplate.opsForHash().get(key, "present");
            long total   = totalStr != null ? Long.parseLong(totalStr) : 0;
            long present = presentStr != null ? Long.parseLong(presentStr) : 0;

            double rate = total == 0 ? 0.0 : (present * 100.0) / total;

            if (rate < 75.0 && !Boolean.TRUE.equals(redisTemplate.hasKey(alertKey))) {
                System.out.println("🚨 Alerting studentId=" + studentId + " rate=" + rate);
                kafkaTemplate.send(
                        "attendance.alert",
                        String.valueOf(studentId),
                        new AlertEvent(studentId, classId, rate)
                );
            }
        }
        //now we have the headCnt and we need to notify the teacher about the same
        notificationService.sendInAppHeadCntNotif(new HeadCntEvent(classId,headCnt,studentIds.size()-headCnt,(long)studentIds.size()));
    }
}
