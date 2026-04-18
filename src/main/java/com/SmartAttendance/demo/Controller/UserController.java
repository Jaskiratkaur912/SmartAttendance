package com.SmartAttendance.demo.Controller;
import com.SmartAttendance.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<?> completeRegistration(@RequestParam String email,@RequestParam String role,@RequestParam(required=false) MultipartFile image) throws Exception {
        String token = userService.completeRegistration(email, role, image);
        return ResponseEntity.ok(token);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam String email) {

        try {
            String response = userService.deleteUser(email);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
