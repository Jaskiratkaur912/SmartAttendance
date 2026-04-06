package com.SmartAttendance.demo.DTO;

import java.time.LocalDate;
import java.util.List;

public class StudentProfileDTO {
    private String studentName;
    private List<LocalDate> attendance;
    public StudentProfileDTO(List<LocalDate> attendance,String studentName){
        this.attendance=attendance;
        this.studentName=studentName;
    }
}