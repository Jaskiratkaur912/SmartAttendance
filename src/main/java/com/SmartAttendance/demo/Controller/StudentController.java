package com.SmartAttendance.demo.Controller;

import com.SmartAttendance.demo.DTO.StudentProfileDTO;
import com.SmartAttendance.demo.Service.EnrollmentService;
import com.SmartAttendance.demo.Service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private StudentProfileService studentProfileService;
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/joinClass")
    public void joinClass(@RequestParam Long studentId,@RequestParam String classCode){
        enrollmentService.joinClass(studentId,classCode);
    }
    @GetMapping("/fetchDetails")
    public ResponseEntity<StudentProfileDTO> fetchStudentProfile(@RequestParam Long studentId, @RequestParam Long classId){
        StudentProfileDTO dto=studentProfileService.fetchProfile(studentId,classId);
        return ResponseEntity.ok(dto);
    }
    
}
