package com.SmartAttendance.demo.Controller;

import com.SmartAttendance.demo.Entities.Attendance;
import com.SmartAttendance.demo.Security.JwtUtil;
import com.SmartAttendance.demo.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private JwtUtil jwtUtil;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(
            @RequestParam String token,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {

        String email = jwtUtil.extractEmail(token);
        Attendance a = attendanceService.markAttendanceByLocation(
            email, latitude, longitude);
        return ResponseEntity.ok(a);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/my")
    public ResponseEntity<List<Attendance>> getMyAttendance(
            @RequestParam String token) {

        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(
            attendanceService.getMyAttendance(email));
    }
}