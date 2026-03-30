package com.example.student_service.service;

import com.example.student_service.entity.StudentImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface StudentImageService {

    // Upload or add image for a student
    StudentImage addImage(Long studentId, MultipartFile file);

    StudentImage addImageByStudentSystemId(Long studentId, MultipartFile file);

    // Get the active image of a student
    Optional<StudentImage> getImageByStudent(Long studentId);

    // Soft delete an image
    void deleteImage(Long imageId);

    // Remove student reference without deleting image
    void detachStudentFromImage(Long imageId);
}