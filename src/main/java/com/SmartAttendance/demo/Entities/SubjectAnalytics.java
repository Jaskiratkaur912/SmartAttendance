package com.SmartAttendance.demo.DTO;

import com.SmartAttendance.demo.Entities.TrendEnum;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jdk.jfr.DataAmount;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"student_id", "class_id"}
        )
)
@Data
public class SubjectAnalytics {
    private Long studentId;
    private Long classId;
    private long attended;
    private long total;
    private double attendancePct;
    private long missable;        // negative = recovery mode
    private double recentPct;
    private double velocity;
    private TrendEnum trend;
    private List<Double> weeklyPcts;   // 8 values for sparkline
    private LocalDateTime computedAt;
}
