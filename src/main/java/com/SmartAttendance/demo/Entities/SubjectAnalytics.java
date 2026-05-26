package com.SmartAttendance.demo.Entities;

import com.SmartAttendance.demo.Entities.TrendEnum;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;

@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"student_id", "class_id"}
        )
)
@Entity


public class SubjectAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private Long classId;
    private String className;
    private long attended;
    private long total;
    private double attendancePct;
    private long missable;        // negative = recovery mode
    private double recentPct;
    private double velocity;
    private TrendEnum trend;
    @ElementCollection
    private List<Double> weeklyPcts;   // 8 values for sparkline
    private LocalDateTime computedAt;
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getAttended() {
        return attended;
    }

    public void setAttended(long attended) {
        this.attended = attended;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public double getAttendancePct() {
        return attendancePct;
    }

    public void setAttendancePct(double attendancePct) {
        this.attendancePct = attendancePct;
    }

    public long getMissable() {
        return missable;
    }

    public void setMissable(long missable) {
        this.missable = missable;
    }

    public double getRecentPct() {
        return recentPct;
    }

    public void setRecentPct(double recentPct) {
        this.recentPct = recentPct;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public TrendEnum getTrend() {
        return trend;
    }

    public void setTrend(TrendEnum trend) {
        this.trend = trend;
    }

    public List<Double> getWeeklyPcts() {
        return weeklyPcts;
    }

    public void setWeeklyPcts(List<Double> weeklyPcts) {
        this.weeklyPcts = weeklyPcts;
    }

    public LocalDateTime getComputedAt() {
        return computedAt;
    }

    public void setComputedAt(LocalDateTime computedAt) {
        this.computedAt = computedAt;
    }
}

