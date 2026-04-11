package com.SmartAttendance.demo.Entities;

import jakarta.persistence.*;
import org.hibernate.generator.internal.GeneratedAlwaysGeneration;

@Entity
public class AssignmentSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
    private String submissionUrl;
    public AssignmentSubmission(){

    }
    public AssignmentSubmission(Assignment assignment,User student,String submissionUrl){
        this.assignment=assignment;
        this.student=student;
        this.submissionUrl=submissionUrl;
    }
    public void setSubmissionUrl(String submissionUrl){
        this.submissionUrl=submissionUrl;
    }
}
