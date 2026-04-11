package com.SmartAttendance.demo.Controller;

import com.SmartAttendance.demo.Entities.Attendance;
import com.SmartAttendance.demo.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // Change this in AttendanceController.java
@PostMapping("/mark")
public ResponseEntity<?> markAttendance(
    @RequestBody Map<String, Double> body,
    @RequestHeader("Authorization") String authHeader) {
    
    String token = authHeader.replace("Bearer ", "");
    String email = jwtUtil.extractEmail(token); // use your JwtUtil
    Attendance a = attendanceService.markAttendance(
        email, body.get("latitude"), body.get("longitude"));
    return ResponseEntity.ok(a);
}

    @GetMapping("/my")
    public ResponseEntity<?> getMyAttendance(
            @AuthenticationPrincipal OAuth2User principal) {

        String email = principal.getAttribute("email");
        return ResponseEntity.ok(
            attendanceService.getMyAttendance(email));
    }
}
