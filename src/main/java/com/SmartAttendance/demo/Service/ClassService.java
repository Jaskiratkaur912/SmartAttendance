package com.SmartAttendance.demo.Service;

import com.SmartAttendance.demo.Entities.ClassRoom;
import com.SmartAttendance.demo.Repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Random;
@Service
public class ClassService {
    @Autowired
    private ClassRepository classRepository;
    //allows a teacher to create class
    public void createClass(Long TeacherId,String className){
        ClassRoom newClass=new ClassRoom(className,TeacherId);
        newClass.setClassCode(generateUniqueCode());
        classRepository.save(newClass);
    }
    private String generateUniqueCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        String code;

        // Keep generating until we get a unique one
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            code = sb.toString();
        } while (classRepository.existsByClassCode(code));

        return code;
    }
    public ClassRoom getClass(String code){
        return classRepository.findByClassCode(code).orElseThrow(()->new RuntimeException("Class not found!"));
    }
    public List<ClassRoom> fetchClasses(Long teacher_id){
        return classRepository.findByTeacherId(teacher_id);
    }
}
