package com.SmartAttendance.demo.Entities;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "doubts")
public class Doubt {
    @Id
    private String id;
    private Long assignmentId;
    private Long studentId;
    private String message;
    private LocalDateTime createdAt;

}
