package com.SmartAttendance.demo.Controller;

import com.SmartAttendance.demo.Config.CloudinaryConfig;
import com.SmartAttendance.demo.DTO.StudentProfileDTO;
import com.SmartAttendance.demo.Entities.Assignment;
import com.SmartAttendance.demo.Entities.ClassRoom;
import com.SmartAttendance.demo.Repository.AssignmentRepository;
import com.SmartAttendance.demo.Repository.ClassRepository;
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
    @Autowired
    private ClassRepository classRepository;
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/joinClass")
    public void joinClass(@RequestParam Long studentId,@RequestParam String classCode){
        enrollmentService.joinClass(studentId,classCode);
    }
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/markAttendance")
    public ResponseEntity<String> markAttendance(@RequestParam Long StudentId, @RequestParam Long classId, @RequestParam MultipartFile image){
        ClassRoom classRoom=classRepository.findById(classId).orElseThrow();
        if(!classRoom.isAttendanceOpen()) return ResponseEntity.badRequest().body("Attendance is not open");
        try {
            attendanceService.markAttendance(StudentId, classId, image);
            return ResponseEntity.ok("Attendance marked successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
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
    public ResponseEntity<String> submitAssignment(@RequestParam Long assignmentId,
                                                   @RequestParam MultipartFile solution,
                                                   @RequestParam Long studentId) {
        try {
            assignmentService.submitAssignment(assignmentId, solution, studentId);
            return ResponseEntity.ok("Submitted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/attendanceStatus")
    public boolean getAttendanceStatus(@RequestParam Long classId){
        ClassRoom classRoom=classRepository.findById(classId).orElseThrow();
        return classRoom.isAttendanceOpen();
    }
    @GetMapping("/getAssignments")
    public List<Assignment> getAssignment(@RequestParam Long classId){
        return assignmentService.fetchAssignment(classId);
    }
    @GetMapping("/assignmentStatus")
    public boolean checkSubmission(@RequestParam Long assignmentId,
                                   @RequestParam Long studentId) {
        return assignmentService.hasSubmitted(assignmentId, studentId);
    }
}
