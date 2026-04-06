package com.SmartAttendance.demo.Service;

import com.SmartAttendance.demo.DTO.StudentProfileDTO;
import com.SmartAttendance.demo.Entities.AttEnum;
import com.SmartAttendance.demo.Entities.Attendance;
import com.SmartAttendance.demo.Repository.AttendanceRepository;
import com.SmartAttendance.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentProfileService {
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    UserRepository userRepository;
    public StudentProfileDTO fetchProfile(Long studentId,Long classId){
        List<Attendance> studAtt=attendanceRepository.findByUserUserIdAndClassRoomClassId(studentId,classId);
        List<LocalDate> presentDates = studAtt.stream()
                .filter(a -> a.getIsPresent() == AttEnum.PRESENT)
                .map(Attendance::getDate)
                .sorted()
                .toList();
        String studName=userRepository.findByuserId(studentId).orElseThrow();
        StudentProfileDTO dto=new StudentProfileDTO(presentDates,studName);
        return dto;
    }

}