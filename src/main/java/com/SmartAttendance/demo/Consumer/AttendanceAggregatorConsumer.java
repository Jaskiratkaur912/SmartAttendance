package com.SmartAttendance.demo.Consumer;
import com.SmartAttendance.demo.KafkaEvent.AttendanceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AttendanceAggregatorConsumer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @KafkaListener(
            topics = "attendance.marked",
            groupId = "attendance-aggregator"
    )

    public void consume(AttendanceEvent event) {
        System.out.println("📥 AttendanceEvent received: studentId=" + event.getStudentId()
                + " classId=" + event.getClassId()
                + " status=" + event.getStatus());

        String key = "att:" + event.getStudentId() + ":" + event.getClassId();

        // ✅ Just set a session flag — SessionClosedConsumer handles totals
        if ("PRESENT".equals(event.getStatus())) {
            String sessionKey = "session:" + event.getClassId() + ":present:" + event.getStudentId();
            redisTemplate.opsForValue().set(sessionKey, "1");
            System.out.println("✅ Session key set: " + sessionKey);
        }

        System.out.println("🔑 Redis key: " + key);
    }
}
