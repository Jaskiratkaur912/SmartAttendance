package com.SmartAttendance.demo.DTO;

import java.time.LocalDate;
import java.util.List;

public class StudentProfileDTO {
    private String studentName;
    private List<LocalDate> attendance;
    private int totalClasses;
    public StudentProfileDTO(List<LocalDate> attendance,String studentName,int totalClasses){
        this.attendance=attendance;
        this.studentName=studentName;
        this.totalClasses=totalClasses;
    }
    public String getStudentName() { return studentName; }
    public List<LocalDate> getAttendance() { return attendance; }

    public int getTotalClasses() {
        return totalClasses;
    }
}