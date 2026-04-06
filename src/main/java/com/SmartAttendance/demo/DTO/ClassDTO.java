package com.SmartAttendance.demo.DTO;

import java.time.LocalDate;
import java.util.List;

public class ClassDTO {
    private String studentName;
    private List<LocalDate> presentDates;
    public ClassDTO(String studentName,List<LocalDate> presentDates){
        this.studentName=studentName;
        this.presentDates=presentDates;
    }
}
