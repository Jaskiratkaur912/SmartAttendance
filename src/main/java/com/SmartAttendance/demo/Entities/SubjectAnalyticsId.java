package com.SmartAttendance.demo.Entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubjectAnalyticsId implements Serializable {
    private Long studentId;
    private Long classId;

    // equals() and hashCode() are REQUIRED
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectAnalyticsId)) return false;
        SubjectAnalyticsId that = (SubjectAnalyticsId) o;
        return Objects.equals(studentId, that.studentId) &&
                Objects.equals(classId, that.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, classId);
    }

    public void setStudentId(Long studId) {
        this.studentId=studentId;
    }
    public void setClassId(Long classId){
        this.classId=classId;
    }
}