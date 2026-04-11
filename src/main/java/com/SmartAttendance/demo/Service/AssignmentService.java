package com.SmartAttendance.demo.Service;

import com.SmartAttendance.demo.Entities.Assignment;
import com.SmartAttendance.demo.Entities.AssignmentSubmission;
import com.SmartAttendance.demo.Entities.User;
import com.SmartAttendance.demo.Repository.AssignmentRepository;
import com.SmartAttendance.demo.Repository.AssignmentSubmissionRepository;
import com.SmartAttendance.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class AssignmentService {
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AssignmentSubmissionRepository assignmentSubmissionRepository;
    @Transactional
    public void submitAssignment(Long assignmentId, MultipartFile solution,Long studentId){
        String url=cloudinaryService.uploadFile(solution,studentId);
        Assignment assignment=assignmentRepository.findById(assignmentId).orElseThrow();
        User student=userRepository.findByUserId(studentId).orElseThrow();
        AssignmentSubmission submission=new AssignmentSubmission(assignment,student,url);
        assignmentSubmissionRepository.save(submission);
    }
}
