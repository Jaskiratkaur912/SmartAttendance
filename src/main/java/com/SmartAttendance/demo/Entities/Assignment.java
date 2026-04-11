package com.SmartAttendance.demo.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long classId;

    private String description;
    private LocalDate deadline;
    private String submissionUrl;
}
