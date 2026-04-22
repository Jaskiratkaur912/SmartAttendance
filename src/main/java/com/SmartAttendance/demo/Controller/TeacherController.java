package com.SmartAttendance.demo.Controller;

import com.SmartAttendance.demo.DTO.ClassDTO;
import com.SmartAttendance.demo.Entities.Assignment;
import com.SmartAttendance.demo.Entities.ClassRoom;
import com.SmartAttendance.demo.Repository.AssignmentRepository;
import com.SmartAttendance.demo.Repository.ClassRepository;
import com.SmartAttendance.demo.Service.AttendanceService;
import com.SmartAttendance.demo.Service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private ClassService classService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private ClassRepository classRepository;
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/createClass")
    public void createClass(@RequestParam Long id,@RequestParam String className){
        classService.createClass(id,className);
    }
    @GetMapping("/fetchClassAttendance")
    public ResponseEntity<List<ClassDTO>> fetchClassAtt(@RequestParam("classId") Long classId){
        List<ClassDTO> classAtt=attendanceService.fetchClassAttendance(classId);
        return ResponseEntity.ok(classAtt);
    }
    @PostMapping("/postAssignment")
    public void postAssignment(@RequestParam Long classId, @RequestParam String description, @RequestParam String deadline) {
        Assignment assignment = new Assignment();
        assignment.setClassId(classId);
        assignment.setDescription(description);
        assignment.setDeadline(LocalDateTime.parse(deadline));
        assignmentRepository.save(assignment);
    }
    @GetMapping("/fetchClasses")
    public List<ClassRoom> fetchClass(@RequestParam Long teacherId){
        return classService.fetchClasses(teacherId);
    }
    @PostMapping("/openAttendance")
    public void openAttendance(@RequestParam Long classId){
        ClassRoom classroom=classRepository.findById(classId).orElseThrow();
        classroom.setAttendanceOpen(true);
        classroom.incClassCount();
        classRepository.save(classroom);
    }
    @PostMapping("/closeAttendance")
    public void closeAttendance(@RequestParam Long classId){
        ClassRoom classRoom=classRepository.findById(classId).orElseThrow();
        classRoom.setAttendanceOpen(false);
        classRepository.save(classRoom);
    }
    @GetMapping("/attendanceStatus")
    public boolean getAttendanceStatus(@RequestParam Long classId){
        ClassRoom classRoom=classRepository.findById(classId).orElseThrow();
        return classRoom.isAttendanceOpen();
    }

}
