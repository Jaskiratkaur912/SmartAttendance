package com.SmartAttendance.demo.Service;
import com.SmartAttendance.demo.Entities.ClassRoom;
import com.SmartAttendance.demo.Entities.User;
import com.SmartAttendance.demo.Repository.UserRepository;
import com.SmartAttendance.demo.Security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Transactional
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
    public String completeRegistration(String email, String role, MultipartFile image) throws Exception {

        // 1. Fetch user
        User user = userRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(role);
        Long id=user.getId();
        // 2. Only for STUDENT → process image + embedding
        if ("STUDENT".equalsIgnoreCase(role)) {

            // 🔹 Upload image to Cloudinary
            String imageUrl = cloudinaryService.uploadFile(image, id);
            user.setImagePath(imageUrl);

            // 🔹 Call FastAPI
            String url = "http://127.0.0.1:8000/register";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            body.add("file", new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, request, Map.class);

            // 🔹 DEBUG (optional but useful)
            System.out.println("FastAPI response: " + response.getBody());

            // 🔹 Validate response
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || responseBody.get("embedding") == null) {
                throw new RuntimeException("Embedding not received from FastAPI: " + responseBody);
            }

            // 🔹 SAFE conversion (VERY IMPORTANT)
            List<?> rawList = (List<?>) responseBody.get("embedding");

            List<Double> embedding = rawList.stream()
                    .map(val -> ((Number) val).doubleValue())
                    .toList();

            // 🔹 Convert to JSON string
            String embeddingJson = objectMapper.writeValueAsString(embedding);

            // 🔹 Store in DB
            user.setEmbeddings(embeddingJson);
        }

        // 3. Finalize registration
        user.setStatus("REGISTERED");
        userRepository.save(user);

        // 4. Generate JWT

        return jwtUtil.generateToken(id,email, role);
    }
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("no user found"));
    }
    @Transactional
    public String deleteUser(String email) {

        User user = userRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
        userRepository.flush();
        return "User deleted successfully";
    }
    public List<ClassRoom> getEnrolledClassrooms(Long userId){
        User user=userRepository.findByuserId(userId).orElseThrow();
        return user.getEnrolledClasses();
    }
}
