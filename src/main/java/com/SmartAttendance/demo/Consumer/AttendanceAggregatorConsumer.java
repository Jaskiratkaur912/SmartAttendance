package com.SmartAttendance.demo.Consumer;

import com.SmartAttendance.demo.KafkaEvent.AlertEvent;
import com.SmartAttendance.demo.KafkaEvent.AttendanceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AttendanceAggregatorConsumer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private KafkaTemplate<String, AlertEvent> kafkaTemplate;

    @KafkaListener(
            topics = "attendance.marked",
            groupId = "attendance-aggregator"
    )
    public void consume(AttendanceEvent event) {

        String key = "att:" + event.getStudentId()
                + ":" + event.getClassId();

        redisTemplate.opsForHash().increment(key, "total", 1);

        if ("PRESENT".equals(event.getStatus())) {
            redisTemplate.opsForHash().increment(key, "present", 1);
        }

        long total   = Long.parseLong((String) redisTemplate
                .opsForHash().get(key, "total"));
        long present = Long.parseLong((String) redisTemplate
                .opsForHash().get(key, "present"));

        double rate = (present * 100.0) / total;

        String alertKey = "alerted:" + event.getStudentId()
                + ":" + event.getClassId();

        if (rate < 75.0 && !redisTemplate.hasKey(alertKey)) {
            AlertEvent alert = new AlertEvent(
                    event.getStudentId(), event.getClassId(), rate);
            kafkaTemplate.send("attendance.alert",
                    String.valueOf(event.getStudentId()), alert);

            redisTemplate.opsForValue()
                    .set(alertKey, "1", Duration.ofHours(24));
        }
    }
}
