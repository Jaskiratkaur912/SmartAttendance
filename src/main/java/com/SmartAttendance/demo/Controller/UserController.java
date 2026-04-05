package com.SmartAttendance.demo.Controller;
import com.SmartAttendance.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/complete-registration")
    public ResponseEntity<Map<String,String>> completeRegistration(@RequestParam String email,@RequestParam String role,@RequestParam(required=false) MultipartFile image){
        String token=userService.completeRegistration(email,role,image);
        return ResponseEntity.ok(Map.of("message","registration completed","token",token));
    }
}
