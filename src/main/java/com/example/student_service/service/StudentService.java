package com.example.student_service.service;

import com.example.student_service.entity.Student;
import com.example.student_service.entity.StudentStatus;

import java.util.List;

public interface StudentService {
    Student createStudent(Student student);
    Student getStudentById(Long id);
    List<StudentStatus> getStudentStatus();
    List<Student> getAllStudents();
    Student updateStudent(Long id, Student student);
    Student migrateStudent(Long id, Student student);
    // StudentService.java (interface)
    void deleteStudent(Long id); // soft delete
}
