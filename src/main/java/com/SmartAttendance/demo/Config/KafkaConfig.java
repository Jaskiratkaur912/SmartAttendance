package com.SmartAttendance.demo.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic attendanceMarkedTopic() {
        return TopicBuilder.name("attendance.marked")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic attendanceAlertTopic() {
        return TopicBuilder.name("attendance.alert")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
