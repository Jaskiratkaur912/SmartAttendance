package com.SmartAttendance.demo.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsConsumer {
    @KafkaListener(topics = "session.closed",
            groupId = "analytics-service")
    public void consume(){
        //this consumer basically loops over all the students to provide them insights on their attendance trends

    }

}
