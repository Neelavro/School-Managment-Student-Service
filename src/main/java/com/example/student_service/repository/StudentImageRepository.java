package com.example.student_service.repository;

import com.example.student_service.entity.StudentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentImageRepository extends JpaRepository<StudentImage, Long> {

    // ✅ Fetch the image of a particular student
    Optional<StudentImage> findByStudentId(Long studentId);

    // ✅ Fetch only active image for a student
    Optional<StudentImage> findByStudentIdAndIsActiveTrue(Long studentId);

    // ✅ Fetch all active images (in case you have multiple per student in the future)
    List<StudentImage> findAllByIsActiveTrue();

    // ✅ Soft delete helper (update isActive)
    default void softDelete(StudentImage image) {
        image.setIsActive(false);
        save(image);
    }
}