package com.SmartAttendance.demo.Service;

import com.SmartAttendance.demo.Entities.ClassRoom;
import com.SmartAttendance.demo.Entities.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {
    //allows a user to join a class
    @Autowired
    private UserService userService;
    @Autowired
    private ClassService classService;
    @Transactional
    public void joinClass(Long student_id,String code){
        User user=userService.getUserById(student_id);
        ClassRoom reqClass=classService.getClass(code);
        user.joinClass(reqClass);
    }

}
