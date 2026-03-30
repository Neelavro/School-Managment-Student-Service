package com.example.student_service.service.impl;

import com.example.student_service.entity.Student;
import com.example.student_service.entity.StudentImage;
import com.example.student_service.repository.StudentImageRepository;
import com.example.student_service.repository.StudentRepository;
import com.example.student_service.service.StudentImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentImageServiceImpl implements StudentImageService {


    private final StudentImageRepository studentImageRepository;
    private final StudentRepository studentRepository;

    private final String IMAGE_FOLDER = "/var/www/student_service/images/";
    private final String BASE_URL = "http://167.172.86.59:8081";

    @Override
    public StudentImage addImage(Long studentId, MultipartFile file) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        String originalFilename = file.getOriginalFilename().replaceAll("\\s+", "_");
        String fileName = UUID.randomUUID() + "_" + originalFilename;
        Path filePath = Paths.get(IMAGE_FOLDER + fileName);

        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }

        StudentImage image = new StudentImage();
        image.setStudent(student);
        image.setImageUrl(BASE_URL + "/images/" + fileName);
        image.setIsActive(true);

        StudentImage saved = studentImageRepository.save(image);

        // ✅ Temp fix: write image_id back on student
        student.setImage(saved);
        studentRepository.save(student);

        return saved;
    }
    @Override
    public StudentImage addImageByStudentSystemId(Long studentId, MultipartFile file) {
        Student student = studentRepository.findByStudentSystemId(studentId.toString())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        String originalFilename = file.getOriginalFilename().replaceAll("\\s+", "_");
        String fileName = UUID.randomUUID() + "_" + originalFilename;
        Path filePath = Paths.get(IMAGE_FOLDER + fileName);

        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }

        StudentImage image = new StudentImage();
        image.setStudent(student);
        image.setImageUrl(BASE_URL + "/images/" + fileName);
        image.setIsActive(true);

        StudentImage saved = studentImageRepository.save(image);

        // ✅ Temp fix: write image_id back on student
        student.setImage(saved);
        studentRepository.save(student);

        return saved;

    }

    @Override
    public Optional<StudentImage> getImageByStudent(Long studentId) {
        return studentImageRepository.findByStudentIdAndIsActiveTrue(studentId);
    }

    @Override
    public void deleteImage(Long imageId) {
        StudentImage image = studentImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));

        // Delete file from disk
        try {
            String fileName = image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1);
            Path filePath = Paths.get(IMAGE_FOLDER + fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }

        // Delete from DB
        studentImageRepository.delete(image);
    }

    @Override
    public void detachStudentFromImage(Long imageId) {
        StudentImage image = studentImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));

        image.setStudent(null);
        studentImageRepository.save(image);
    }
}