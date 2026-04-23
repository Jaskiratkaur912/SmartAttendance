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
            String originalName = file.getOriginalFilename();
            String publicId = "smart-attendance/assignments/" + studentId + "_" + System.currentTimeMillis() + "_" + originalName;

            Map result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "resource_type", "raw"  // 🔥 IMPORTANT for pdf/doc
                    )
            );

            String url = result.get("secure_url").toString();
            System.out.println("UPLOAD URL: " + url);
            return url;

        } catch (IOException e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }
}