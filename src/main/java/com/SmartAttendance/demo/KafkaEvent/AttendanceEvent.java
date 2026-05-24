package com.SmartAttendance.demo.KafkaEvent;

public class AttendanceEvent {
    private Long studentId;
    private Long classId;
    private String status;    // "PRESENT" or "ABSENT"
    private long timestamp;
    public AttendanceEvent() {}
    public AttendanceEvent(Long studentId, Long classId, String status, long timestamp) {
        this.studentId = studentId;
        this.classId   = classId;
        this.status    = status;
        this.timestamp = timestamp;
    }
    public Long getStudentId(){return this.studentId;}
    public Long getClassId(){return this.classId;}
    public String getStatus(){return this.status;}
    public long getTimestamp() { return this.timestamp; }
    public void setStatus(String status){
        this.status=status;
    }
    public void setStudentId(Long studentId){
        this.studentId = studentId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
