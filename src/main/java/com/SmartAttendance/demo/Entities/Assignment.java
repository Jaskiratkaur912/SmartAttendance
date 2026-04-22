package com.SmartAttendance.demo.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long classId;
    private String description;
    private LocalDateTime deadline;
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentSubmission> submissions;
    public Assignment(){

    }

    public LocalDateTime getDeadline() { return this.deadline;
    }
    public Long getId() { return id; }
    public Long getClassId() { return classId; }
    public String getDescription() { return description; }
    public List<AssignmentSubmission> getSubmissions() { return submissions; }
    public void setClassId(Long classId) { this.classId = classId; }
    public void setDescription(String description) { this.description = description; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }
}
