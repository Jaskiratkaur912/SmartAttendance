package com.SmartAttendance.demo.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

    @Entity
    public class ClassRoom {
        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        private Long classId;
        private String className;
        @Column(name = "teacher_id")
        private Long teacherId;
        @Column(unique=true)
        private String classCode;
        @JsonIgnore
        @ManyToMany(mappedBy = "classesJoined")
        private List<User> enrolledStudents = new ArrayList<>();
        private int totalClasses;
        private boolean attendanceOpen;
        public ClassRoom(){

        }
    public ClassRoom(String className, Long teacher_id){
        this.className=className;
        this.teacherId=teacher_id;
    }
    public void setClassCode(String classCode){
        this.classCode=classCode;
    }
    public void setAttendanceOpen(boolean val){
        this.attendanceOpen=val;
    }
    public void incClassCount(){this.totalClasses+=1;}
    public boolean isAttendanceOpen(){
        return this.attendanceOpen;
    }
    public Long getClassId() { return classId; }
    public String getClassName() { return className; }
    public String getClassCode() { return classCode; }
    public Long getTeacherId() { return teacherId; }
    public int getTotalClasses() { return totalClasses; }
    public List<User> getEnrolledStudents() { return enrolledStudents; }
    public int getEnrolledStudentCnt(){
            return getEnrolledStudents().size();
    }
}
