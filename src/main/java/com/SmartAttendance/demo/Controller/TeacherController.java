package com.SmartAttendance.demo.Controller;

import com.SmartAttendance.demo.Service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private ClassService classService;
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/createClass")
    public void createClass(@RequestParam Long id,@RequestParam String className){
        classService.createClass(id,className);
    }
}
