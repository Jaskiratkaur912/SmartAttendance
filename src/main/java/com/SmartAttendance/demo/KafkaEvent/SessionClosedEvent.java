package com.SmartAttendance.demo.KafkaEvent;

public class SessionClosedEvent {
    private Long classId;
    public SessionClosedEvent(){}
    public SessionClosedEvent(Long classId){
        this.classId=classId;
    }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

}
