package com.SmartAttendance.demo.Service;

import com.SmartAttendance.demo.DTO.ClassDTO;
import com.SmartAttendance.demo.Entities.AttEnum;
import com.SmartAttendance.demo.Entities.Attendance;
import com.SmartAttendance.demo.Entities.ClassRoom;
import com.SmartAttendance.demo.Entities.User;
import com.SmartAttendance.demo.Repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    @Autowired
    AttendanceRepository attendanceRepository;
    public void markAttendance(Long studId, Long classId, MultipartFile image){

    }
    public List<ClassDTO> fetchClassAttendance(Long classId) {

        return attendanceRepository.findByClassRoomClassId(classId)
                .stream()
                .collect(Collectors.groupingBy(a -> a.getUser()))
                .values()
                .stream()
                .map(records -> {

                    User student = records.get(0).getUser();

                    List<LocalDate> presentDates = records.stream()
                            .filter(a -> a.getIsPresent() == AttEnum.PRESENT)
                            .map(Attendance::getDate)
                            .sorted()
                            .toList();

                    return new ClassDTO(
                            student.getName(),
                            presentDates
                    );
                })
                .toList();
    }
}