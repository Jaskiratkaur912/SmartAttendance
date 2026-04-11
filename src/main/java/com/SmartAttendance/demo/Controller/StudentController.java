package com.SmartAttendance.demo.Controller;

import com.SmartAttendance.demo.Config.CloudinaryConfig;
import com.SmartAttendance.demo.DTO.StudentProfileDTO;
import com.SmartAttendance.demo.Entities.Assignment;
import com.SmartAttendance.demo.Entities.ClassRoom;
import com.SmartAttendance.demo.Repository.AssignmentRepository;
import com.SmartAttendance.demo.Service.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private CloudinaryService cloudinaryService;
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
    @PostMapping("/submitAssignment")
    public void submitAssignment(@RequestParam Long assignmentId,@RequestParam MultipartFile solution,@RequestParam Long studentId) throws IOException {
        assignmentService.submitAssignment(assignmentId,solution,studentId);
    }

    
}
