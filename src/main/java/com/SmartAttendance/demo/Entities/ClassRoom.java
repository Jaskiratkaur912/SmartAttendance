package com.SmartAttendance.demo.Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ClassRoom {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long classId;
    private String className;
    private Long teacher_id;
    @Column(unique=true)
    private String classCode;
    @ManyToMany(mappedBy = "classesJoined")
    private List<User> enrolledStudents = new ArrayList<>();
    private int totalClasses;
    private boolean attendanceOpen;
    public ClassRoom(){

    }
    public ClassRoom(String className, Long teacher_id){
        this.className=className;
        this.teacher_id=teacher_id;
    }
    public void setClassCode(String classCode){
        this.classCode=classCode;
    }
    public void setAttendanceOpen(boolean val){
        this.attendanceOpen=val;
    }
    public void incClassCount(){this.totalClasses+=1;}


}
