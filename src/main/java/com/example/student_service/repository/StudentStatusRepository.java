package com.example.student_service.repository;

import com.example.student_service.entity.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentStatusRepository extends JpaRepository<StudentStatus, Integer> {


}
