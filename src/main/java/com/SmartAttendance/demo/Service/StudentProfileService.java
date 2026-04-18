package com.SmartAttendance.demo.Service;

import com.SmartAttendance.demo.DTO.StudentProfileDTO;
import com.SmartAttendance.demo.Entities.AttEnum;
import com.SmartAttendance.demo.Entities.Attendance;
import com.SmartAttendance.demo.Entities.ClassRoom;
import com.SmartAttendance.demo.Entities.User;
import com.SmartAttendance.demo.Repository.AttendanceRepository;
import com.SmartAttendance.demo.Repository.ClassRepository;
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
    @Autowired
    ClassRepository classRepository;
    public StudentProfileDTO fetchProfile(Long studentId,Long classId){
        List<Attendance> studAtt=attendanceRepository.findByUserUserIdAndClassRoomClassId(studentId,classId);
        List<LocalDate> presentDates = studAtt.stream()
                .filter(a -> a.getIsPresent() == AttEnum.PRESENT)
                .map(Attendance::getDate)
                .sorted()
                .toList();
        ClassRoom classRoom=classRepository.findById(classId).orElseThrow();
        int totalClasses=classRoom.getTotalClasses();
        User user=userRepository.findByUserId(studentId).orElseThrow();
        String studName=user.getName();
        StudentProfileDTO dto=new StudentProfileDTO(presentDates,studName,totalClasses);
        return dto;
    }

}