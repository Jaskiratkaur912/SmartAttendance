package com.SmartAttendance.demo.KafkaEvent;

public class AttendanceEvent {
    private Long studentId;
    private Long classId;
    private String status;    // "PRESENT" or "ABSENT"
    private long timestamp;
    public AttendanceEvent() {}
    public AttendanceEvent(Long id, Long classId, String name, long l) {
        this.studentId = studentId;
        this.classId   = classId;
        this.status    = status;
        this.timestamp = timestamp;
    }
    public Long getStudentId(){return this.studentId;}
    public Long getClassId(){return this.classId;}
    public String getStatus(){return this.status;}
}
