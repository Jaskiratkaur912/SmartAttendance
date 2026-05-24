package com.SmartAttendance.demo.KafkaEvent;

public class HeadCntEvent {
    private Long classId;
    private Long present;
    private Long absent;
    private Long total;

    public HeadCntEvent(){}
    public HeadCntEvent(Long classId,Long present,Long absent,Long total){
        this.classId=classId;
        this.present=present;
        this.absent=absent;
        this.total=total;
    }
    public Long getClassId(){
        return this.classId;
    }
    public Long getPresent(){
        return this.present;
    }
    public Long getAbsent(){
        return this.absent;
    }
    public Long getTotal(){
        return this.total;
    }
    public void setClassId(Long classId){
        this.classId=classId;
    }
    public void setPresent(Long present){
        this.present=present;
    }
    public void setAbsent(Long absent){
        this.absent=absent;
    }
    public void setTotal(Long total){
        this.total=total;
    }
}
