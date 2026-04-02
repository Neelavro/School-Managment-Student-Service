package com.example.student_service.service.impl;

import com.example.student_service.entity.*;
import com.example.student_service.repository.*;
import com.example.student_service.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final GenderRepository genderRepository;
    private final StudentStatusRepository studentStatusRepository;

    // Generate unique 8-digit student_system_id
    private String generateStudentSystemId() {
        String yearPrefix = String.valueOf(java.time.Year.now().getValue());
        String maxId = studentRepository.findMaxStudentSystemIdByYear(yearPrefix);

        int nextNumber;
        if (maxId == null || maxId.isEmpty()) {
            nextNumber = 1;
        } else {
            String lastFourDigits = maxId.substring(4);
            nextNumber = Integer.parseInt(lastFourDigits) + 1;
        }

        return yearPrefix + String.format("%04d", nextNumber);
    }

    private void assignOrUpdateStudentSystemId(Student student, Student existing) {
        String newId = student.getStudentSystemId();

        if (newId == null || newId.isBlank()) {
            existing.setStudentSystemId(generateStudentSystemId());
        } else if (!newId.equals(existing.getStudentSystemId())) {
            if (studentRepository.existsByStudentSystemId(newId)) {
                throw new IllegalArgumentException("studentSystemId already exists: " + newId);
            }
            existing.setStudentSystemId(newId);
        }
    }

    @Override
    public Student createStudent(Student student) {
        student.setIsActive(true);
        assignOrUpdateStudentSystemId(student, student);

        if (student.getGender() != null) {
            Gender gender = genderRepository.getReferenceById(student.getGender().getId());
            student.setGender(gender);
        }

        if (student.getStudentStatus() != null) {
            StudentStatus studentStatus = studentStatusRepository.getReferenceById(student.getStudentStatus().getId());
            student.setStudentStatus(studentStatus);
        }

        return studentRepository.save(student);
    }

    @Override
    public Student getStudentBySystemId(String studentSystemId) {
        return studentRepository.findByStudentSystemId(studentSystemId)
                .filter(Student::getIsActive)
                .orElse(null);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .filter(Student::getIsActive)
                .orElse(null);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .filter(Student::getIsActive)
                .toList();
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        return studentRepository.findById(id)
                .map(existing -> {
                    mapSimpleFields(student, existing);
                    assignOrUpdateStudentSystemId(student, existing);

                    if (student.getGender() != null) {
                        existing.setGender(genderRepository.getReferenceById(student.getGender().getId()));
                    }

                    if (student.getStudentStatus() != null) {
                        existing.setStudentStatus(studentStatusRepository.getReferenceById(student.getStudentStatus().getId()));
                    }

                    return studentRepository.save(existing);
                })
                .orElse(null);
    }

    @Override
    public Student migrateStudent(Long id, Student student) {
        return studentRepository.findById(id)
                .map(existing -> {
                    mapSimpleFields(student, existing);

                    existing.setGender(
                            student.getGender() != null
                                    ? genderRepository.getReferenceById(student.getGender().getId())
                                    : null
                    );

                    existing.setStudentStatus(
                            student.getStudentStatus() != null
                                    ? studentStatusRepository.getReferenceById(student.getStudentStatus().getId())
                                    : null
                    );

                    existing.setImage(student.getImage());

                    return studentRepository.save(existing);
                })
                .orElse(null);
    }

    @Override
    public List<StudentStatus> getStudentStatus() {
        return studentStatusRepository.findAll();
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setIsActive(false);
        studentRepository.save(student);
    }

    // ── helpers ────────────────────────────────────────────────────────────────

    private void mapSimpleFields(Student src, Student dest) {
        dest.setClassRoll(src.getClassRoll());
        dest.setNameBangla(src.getNameBangla());
        dest.setNameEnglish(src.getNameEnglish());
        dest.setFatherNameBangla(src.getFatherNameBangla());
        dest.setFatherNameEnglish(src.getFatherNameEnglish());
        dest.setFatherOccupation(src.getFatherOccupation());
        dest.setFatherPhone(src.getFatherPhone());
        dest.setFatherMonthlySalary(src.getFatherMonthlySalary());
        dest.setMotherNameBangla(src.getMotherNameBangla());
        dest.setMotherNameEnglish(src.getMotherNameEnglish());
        dest.setMotherOccupation(src.getMotherOccupation());
        dest.setMotherPhone(src.getMotherPhone());
        dest.setMotherMonthlySalary(src.getMotherMonthlySalary());
        dest.setGuardianNameBangla(src.getGuardianNameBangla());
        dest.setGuardianNameEnglish(src.getGuardianNameEnglish());
        dest.setGuardianOccupation(src.getGuardianOccupation());
        dest.setGuardianPhone(src.getGuardianPhone());
        dest.setGuardianRelation(src.getGuardianRelation());
        dest.setCurrentHoldingNo(src.getCurrentHoldingNo());
        dest.setCurrentRoadOrVillage(src.getCurrentRoadOrVillage());
        dest.setCurrentDistrict(src.getCurrentDistrict());
        dest.setCurrentThana(src.getCurrentThana());
        dest.setPermanentHoldingNo(src.getPermanentHoldingNo());
        dest.setPermanentRoadOrVillage(src.getPermanentRoadOrVillage());
        dest.setPermanentDistrict(src.getPermanentDistrict());
        dest.setPermanentThana(src.getPermanentThana());
        dest.setDob(src.getDob());
        dest.setNationality(src.getNationality());
        dest.setIsActive(src.getIsActive());
    }
}