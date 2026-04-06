package com.SmartAttendance.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FastAPIconfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
