package com.SmartAttendance.demo.Service;

import com.SmartAttendance.demo.DTO.ClassDTO;
import com.SmartAttendance.demo.Entities.AttEnum;
import com.SmartAttendance.demo.Entities.Attendance;
import com.SmartAttendance.demo.Entities.ClassRoom;
import com.SmartAttendance.demo.Entities.User;
import com.SmartAttendance.demo.Repository.AttendanceRepository;
import com.SmartAttendance.demo.Repository.ClassRepository;
import com.SmartAttendance.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
@Service
public class AttendanceService {
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    RestTemplate restTemplate;
    public void markAttendance(Long studId, Long classId, MultipartFile image){
        User user=userRepository.findByUserId(studId).orElseThrow(()->new RuntimeException("User not found"));
        ClassRoom classRoom=classRepository.findById(classId).orElseThrow(()->new RuntimeException("class not found"));
        //we now need to check if the student has marked the attendance for this class already
        LocalDate today=LocalDate.now();
        Optional<Attendance> existing=attendanceRepository.findByUser_UserIdAndClassRoom_ClassIdAndDate(studId,classId,today);
        if(existing.isPresent()){
            throw new RuntimeException("Attendance already registered for today!");
        }
        try{
            String url="http://127.0.0.1:8000/verify";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // 🔹 image
            body.add("file", new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            });

            // 🔹 stored embedding (VERY IMPORTANT)
            body.add("stored_embedding", user.getEmbeddings());

            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, request, Map.class);

            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null) {
                throw new RuntimeException("No response from FastAPI");
            }

            // 🔥 Read match result
            Boolean match = (Boolean) responseBody.get("match");

            if (match == null) {
                throw new RuntimeException("Invalid response: " + responseBody);
            }

            System.out.println("FastAPI response: " + responseBody);

            // 5. Save attendance
            Attendance attendance = new Attendance();
            attendance.setUser(user);
            attendance.setClassRoom(classRoom);
            attendance.setDate(today);

            if (match) {
                attendance.setIsPresent(AttEnum.PRESENT);
                attendanceRepository.save(attendance);
            } else {
                attendance.setIsPresent(AttEnum.ABSENT);
                attendanceRepository.save(attendance);
                throw new RuntimeException("Face verification failed. Attendance marked as absent.");
            }

            attendanceRepository.save(attendance);

        } catch (Exception e) {
            throw new RuntimeException("Attendance failed: " + e.getMessage());

        }



    }
    public List<ClassDTO> fetchClassAttendance(Long classId) {

        return attendanceRepository.findByClassRoomClassId(classId)
                .stream()
                .collect(Collectors.groupingBy(a -> a.getUser()))
                .values()
                .stream()
                .map(records -> {

                    User student = records.get(0).getUser();

                    List<LocalDate> presentDates = records.stream()
                            .filter(a -> a.getIsPresent() == AttEnum.PRESENT)
                            .map(Attendance::getDate)
                            .sorted()
                            .toList();

                    return new ClassDTO(
                            student.getName(),
                            presentDates
                    );
                })
                .toList();
    }
    public Attendance markAttendanceByLocation(String email, Double latitude, Double longitude) {
    User user = userRepository.findByEmailId(email)
        .orElseThrow(() -> new RuntimeException("User not found"));

    LocalDate today = LocalDate.now();

    Attendance attendance = new Attendance();
    attendance.setUser(user);
    attendance.setDate(today);
    attendance.setLatitude(latitude);
    attendance.setLongitude(longitude);
    attendance.setIsPresent(AttEnum.PRESENT);

    return attendanceRepository.save(attendance);
}

public List<Attendance> getMyAttendance(String email) {
    User user = userRepository.findByEmailId(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return attendanceRepository.findByUser(user);
}
}