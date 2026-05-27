package com.SmartAttendance.demo.Entities;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class DailyAnalyticsPts {
    private LocalDate date;
    private Double cumulativePct;
    public DailyAnalyticsPts(){}
    public DailyAnalyticsPts(LocalDate date,Double cumulativePct){
        this.date=date;
        this.cumulativePct=cumulativePct;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public Double getCumulativePct(){
        return this.cumulativePct;
    }
    public void setDate(LocalDate date){
        this.date=date;
    }
    public void setCumulativePct(Double cumulativePct){
        this.cumulativePct=cumulativePct;
    }
}
