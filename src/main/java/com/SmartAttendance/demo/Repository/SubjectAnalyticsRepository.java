package com.SmartAttendance.demo.Repository;


import com.SmartAttendance.demo.Entities.SubjectAnalytics;
import com.SmartAttendance.demo.Entities.SubjectAnalyticsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectAnalyticsRepository extends JpaRepository<SubjectAnalytics, SubjectAnalyticsId> {

}
