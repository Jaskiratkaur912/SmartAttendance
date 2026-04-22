package com.SmartAttendance.demo.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file, Long studentId) {
        try {
            String originalName = file.getOriginalFilename(); // e.g. assignment.pdf

            String publicId = "student_" + studentId + "_" + System.currentTimeMillis();

            Map result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "smart-attendance/assignments",
                            "resource_type", "raw",
                            "public_id", publicId,
                            "format", originalName.substring(originalName.lastIndexOf(".") + 1) // 🔥 KEY FIX
                    )
            );

            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }
}