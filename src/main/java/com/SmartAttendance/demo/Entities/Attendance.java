package com.SmartAttendance.demo.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="class_id")
    private ClassRoom classRoom;
    private LocalDateTime timestamp;  // ✅ new
    private Double latitude;          // ✅ new
    private Double longitude;

    private LocalDate date;
    private AttEnum isPresent;
    public AttEnum getIsPresent(){
        return this.isPresent;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public User getUser(){
        return this.user;
    }
    public LocalDateTime getTimestamp() { return this.timestamp; }
    public Double getLatitude() { return this.latitude; }
    public Double getLongitude() { return this.longitude; }
    //settters
    public void setUser(User user){
        this.user=user;
    }
    public void setClassRoom(ClassRoom classRoom){
        this.classRoom=classRoom;
    }
    public void setIsPresent(AttEnum attEnum){
        this.isPresent=attEnum;
    }
    public void setDate(LocalDate date){
        this.date=date;
    }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

}
