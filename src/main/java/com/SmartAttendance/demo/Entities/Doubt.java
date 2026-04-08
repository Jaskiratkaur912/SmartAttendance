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
    private String question;
    private String resolution;
    private LocalDateTime createdAt;
    private DoubtStatus status;
    public Doubt(){

    }
    public DoubtStatus getStatus(){
        return this.status;
    }
    public void setStatus(DoubtStatus status){
        this.status=status;
    }
    public void setResolution(String resolution){
        this.resolution=resolution;
    }
    public void setAssignmentId(Long id){this.assignmentId=id;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}

}
