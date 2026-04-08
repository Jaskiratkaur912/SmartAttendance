package com.SmartAttendance.demo.Controller;

import com.SmartAttendance.demo.DTO.StudentProfileDTO;
import com.SmartAttendance.demo.Entities.ClassRoom;
import com.SmartAttendance.demo.Service.AttendanceService;
import com.SmartAttendance.demo.Service.EnrollmentService;
import com.SmartAttendance.demo.Service.StudentProfileService;
import com.SmartAttendance.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private UserService userService;
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/joinClass")
    public void joinClass(@RequestParam Long studentId,@RequestParam String classCode){
        enrollmentService.joinClass(studentId,classCode);
    }
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/markAttendance")
    public void markAttendance(@RequestParam Long StudentId, @RequestParam Long classId, @RequestParam MultipartFile image){
        attendanceService.markAttendance(StudentId,classId,image);
    }
    @GetMapping("/fetchDetails")
    public ResponseEntity<StudentProfileDTO> fetchStudentProfile(@RequestParam Long studentId, @RequestParam Long classId){
        StudentProfileDTO dto=studentProfileService.fetchProfile(studentId,classId);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/getEnrolledClasses")
    public ResponseEntity<List<ClassRoom>> fetchEnrolledClasses(@RequestParam Long studentId){
        List<ClassRoom> enrolledClasses=userService.getEnrolledClassrooms(studentId);
        return ResponseEntity.ok(enrolledClasses);

    }
    
}
