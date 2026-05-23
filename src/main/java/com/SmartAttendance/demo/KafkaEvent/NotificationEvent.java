package com.SmartAttendance.demo.KafkaEvent;

public class NotificationEvent {
    private Long studentId;
    private String message;
    private String type;        // "ATTENDANCE_ALERT"
    private long timestamp;

    public NotificationEvent() {}

    public NotificationEvent(Long studentId, String message, String type) {
        this.studentId = studentId;
        this.message   = message;
        this.type      = type;
        this.timestamp = System.currentTimeMillis();
    }
    public Long getStudentId(){return this.studentId;}
    public String getMessage(){return this.message;}

}