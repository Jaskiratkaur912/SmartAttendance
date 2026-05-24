package com.SmartAttendance.demo.KafkaEvent;

public class HeadCntEvent {
    private Long classId;
    private Long present;
    public HeadCntEvent(){}
    public HeadCntEvent(Long classId,Long present){
        this.classId=classId;
        this.present=present;
    }
    public Long getClassId(){
        return this.classId;
    }
    public Long getPresent(){
        return this.present;
    }
    public void setClassId(Long classId){
        this.classId=classId;
    }
    public void setPresent(Long present){
        this.present=present;
    }
}
