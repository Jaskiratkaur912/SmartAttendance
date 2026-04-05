package com.SmartAttendance.demo.Service;
import com.SmartAttendance.demo.Entities.User;
import com.SmartAttendance.demo.Repository.UserRepository;
import com.SmartAttendance.demo.Security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private JwtUtil jwtUtil;

    public User processOAuthUser(String email, String name){
        Optional<User> existingUser=userRepository.findByEmailId(email);
        if(existingUser.isPresent()){
            return existingUser.get();
        }
        //creating new user (case of registration)
        User user=new User(name,email,null,"INCOMPLETE");
        return userRepository.save(user);
    }
    @Transactional
    public String completeRegistration(String email, String role, MultipartFile image){
        User user=userRepository.findByEmailId(email).orElseThrow(()->new RuntimeException("User not found"));
        user.setRole(role);
        //working with the image if student registration
        if(role.equals("STUDENT")){
            //upload student image to cloudinary
            String imageUrl=cloudinaryService.uploadImage(image,email);
            user.setImagePath(imageUrl);
            //now we need to extract face embeddings from this picture
        }
        user.setStatus("REGISTERED");
        userRepository.save(user);
        return jwtUtil.generateToken(email, role);
    }
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("no user found"));
    }
}
