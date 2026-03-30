package com.example.student_service.controller;

import com.example.student_service.entity.StudentImage;
import com.example.student_service.service.StudentImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/students/images")
@RequiredArgsConstructor
public class StudentImageController {

    private final StudentImageService studentImageService;

    // ✅ Upload an image for a student
    @PostMapping("/{studentId}")
    public ResponseEntity<StudentImage> uploadImage(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file
    ) {
        StudentImage image = studentImageService.addImage(studentId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(image);
    }

    @PostMapping("/seed/{studentId}")
    public ResponseEntity<StudentImage> uploadImageSeed(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file
    ) {
        StudentImage image = studentImageService.addImageByStudentSystemId(studentId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(image);
    }

    // ✅ Get the active image of a student
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentImage> getActiveImage(@PathVariable Long studentId) {
        Optional<StudentImage> imageOpt = studentImageService.getImageByStudent(studentId);
        return imageOpt
                .map(image -> ResponseEntity.ok().body(image))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Soft delete an image
    @DeleteMapping("/soft-delete/{imageId}")
    public ResponseEntity<String> softDeleteImage(@PathVariable Long imageId) {
        studentImageService.deleteImage(imageId);
        return ResponseEntity.ok("Image soft deleted successfully");
    }

    // ✅ Detach student from an image without deleting the image
    @PatchMapping("/detach/{imageId}")
    public ResponseEntity<String> detachStudent(@PathVariable Long imageId) {
        studentImageService.detachStudentFromImage(imageId);
        return ResponseEntity.ok("Student detached from image successfully");
    }
}