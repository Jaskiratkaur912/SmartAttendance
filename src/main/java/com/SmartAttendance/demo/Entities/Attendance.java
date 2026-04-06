package com.SmartAttendance.demo.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;
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
}