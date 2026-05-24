package com.SmartAttendance.demo.KafkaEvent;

public class AlertEvent {
    private Long studentId;
    private Long classId;
    private double rate;

    public AlertEvent() {}

    public AlertEvent(Long studentId, Long classId, double rate) {
        this.studentId = studentId;
        this.classId   = classId;
        this.rate      = rate;
    }
    public Long getClassId(){return this.classId;}
    public Long getStudentId(){return this.studentId;}
    public double getRate(){return this.rate;}

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}