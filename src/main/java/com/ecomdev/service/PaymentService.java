package com.ecomdev.service;

import com.ecomdev.dto.payment.PaymentRequestDTO;
import com.ecomdev.dto.payment.PaymentResponseDTO;
import com.ecomdev.entity.PaymentEntity;
import com.ecomdev.entity.StudentEntity;
import com.ecomdev.repository.PaymentRepository;
import com.ecomdev.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;

    // 1. Yangi to'lov kiritish
    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        StudentEntity student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("O'quvchi topilmadi. ID: " + dto.getStudentId()));

        PaymentEntity payment = PaymentEntity.builder()
                .student(student)
                .amount(dto.getAmount())
                .paidDate(dto.getPaidDate())
                .build();

        PaymentEntity saved = paymentRepository.save(payment);
        return mapToDTO(saved);
    }

    // 2. O'quvchi bo'yicha to'lovlar tarixi
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getPaymentsByStudentId(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("O'quvchi topilmadi. ID: " + studentId);
        }

        return paymentRepository.findByStudentIdOrderByPaidDateDesc(studentId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Entity -> DTO mapper
    private PaymentResponseDTO mapToDTO(PaymentEntity payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .studentId(payment.getStudent().getId())
                .studentName(payment.getStudent().getFullName())
                .amount(payment.getAmount())
                .paidDate(payment.getPaidDate())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}