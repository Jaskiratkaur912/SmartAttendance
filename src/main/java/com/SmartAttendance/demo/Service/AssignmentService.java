package com.SmartAttendance.demo.Service;

import com.SmartAttendance.demo.Entities.Assignment;
import com.SmartAttendance.demo.Entities.AssignmentSubmission;
import com.SmartAttendance.demo.Entities.User;
import com.SmartAttendance.demo.Repository.AssignmentRepository;
import com.SmartAttendance.demo.Repository.AssignmentSubmissionRepository;
import com.SmartAttendance.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
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
    public void submitAssignment(Long assignmentId, MultipartFile solution, Long studentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
        User student = userRepository.findById(studentId).orElseThrow();
        // 🚨 prevent multiple submissions
        List<AssignmentSubmission> existing =
                assignmentSubmissionRepository.findByAssignmentAndStudent(assignment, student);
        if (existing.size()>0) {
            throw new RuntimeException("Already submitted!");
        }
        // 🚨 deadline check
        if (assignment.getDeadline() != null &&
                LocalDateTime.now().isAfter(assignment.getDeadline())) {
            throw new RuntimeException("Deadline has passed. Submission not allowed.");
        }

        String url = cloudinaryService.uploadFile(solution, studentId);
        String originalName = solution.getOriginalFilename();

        AssignmentSubmission submission =
                new AssignmentSubmission(assignment, student, url);
        submission.setOriginalFileName(originalName);
        assignmentSubmissionRepository.save(submission);
    }

    // ✅ FETCH ASSIGNMENTS (existing)
    public List<Assignment> fetchAssignment(Long classId) {
        return assignmentRepository.findByClassId(classId);
    }

    // ✅ TEACHER: get all submissions
    public List<AssignmentSubmission> getSubmissions(Long assignmentId) {
        return assignmentSubmissionRepository
                .findByAssignment_Id(assignmentId);
    }

    // ✅ STUDENT: check submission status
    public boolean hasSubmitted(Long assignmentId, Long studentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
        User student = userRepository.findById(studentId).orElseThrow();

        return !assignmentSubmissionRepository
                .findByAssignmentAndStudent(assignment, student).isEmpty();
    }
}