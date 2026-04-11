package com.example.student_service.service;

import com.example.student_service.entity.Student;
import com.example.student_service.entity.StudentStatus;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student createStudent(Student student);
    Student getStudentById(Long id);
    List<StudentStatus> getStudentStatus();
    List<Student> getAllStudents();
    Student updateStudent(Long id, Student student);
    Student migrateStudent(Long id, Student student);
    // StudentService.java (interface)
    Student getStudentBySystemId(String studentSystemId);
    void deleteStudent(Long id); // soft delete
    Student updateStudentBySystemId(String studentSystemId, Student student);

}
