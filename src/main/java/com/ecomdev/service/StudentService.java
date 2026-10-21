package com.ecomdev.service;

import com.ecomdev.dto.student.StudentRequestDTO;
import com.ecomdev.dto.student.StudentResponseDTO;
import com.ecomdev.entity.StudentEntity;
import com.ecomdev.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    // 1. Yangi o'quvchi qo'shish
    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        if (studentRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalArgumentException("Ushbu telefon raqamli o'quvchi allaqachon mavjud: " + dto.getPhoneNumber());
        }

        StudentEntity student = StudentEntity.builder()
                .fullName(dto.getFullName())
                .phoneNumber(dto.getPhoneNumber())
                .groupName(dto.getGroupName())
                .monthlyFee(dto.getMonthlyFee())
                .build();

        StudentEntity saved = studentRepository.save(student);
        return mapToDTO(saved);
    }

    // 2. Barcha o'quvchilar ro'yxati
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // 3. ID bo'yicha bitta o'quvchini olish
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O'quvchi topilmadi. ID: " + id));
        return mapToDTO(student);
    }

    // 4. O'quvchi ma'lumotlarini yangilash (Edit)
    @Transactional
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {
        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O'quvchi topilmadi. ID: " + id));

        student.setFullName(dto.getFullName());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setGroupName(dto.getGroupName());
        student.setMonthlyFee(dto.getMonthlyFee());

        StudentEntity updated = studentRepository.save(student);
        return mapToDTO(updated);
    }

    // 5. O'quvchini o'chirish (Delete)
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("O'chirish uchun o'quvchi topilmadi. ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    // Entity -> DTO mapper yordamchi metodi
    private StudentResponseDTO mapToDTO(StudentEntity student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .phoneNumber(student.getPhoneNumber())
                .groupName(student.getGroupName())
                .monthlyFee(student.getMonthlyFee())
                .createdAt(student.getCreatedAt())
                .build();
    }
}