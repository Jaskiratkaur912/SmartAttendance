package com.SmartAttendance.demo.Controller;

import com.SmartAttendance.demo.DTO.ClassDTO;
import com.SmartAttendance.demo.Service.AttendanceService;
import com.SmartAttendance.demo.Service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private ClassService classService;
    @Autowired
    private AttendanceService attendanceService;
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/createClass")
    public void createClass(@RequestParam Long id,@RequestParam String className){
        classService.createClass(id,className);
    }
    @GetMapping("/fetchClassAttendance")
    public ResponseEntity<List<ClassDTO>> fetchClassAtt(@RequestParam Long classId){
        List<ClassDTO> classAtt=attendanceService.fetchClassAttendance(classId);
        return ResponseEntity.ok(classAtt);
    }

}
