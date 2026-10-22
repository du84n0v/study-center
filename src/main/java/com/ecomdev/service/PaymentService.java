package com.ecomdev.service;

import com.ecomdev.dto.payment.PaymentRequestDTO;
import com.ecomdev.dto.payment.PaymentResponseDTO;
import com.ecomdev.entity.PaymentEntity;
import com.ecomdev.entity.StudentEntity;
import com.ecomdev.exception.AppBadRequestException;
import com.ecomdev.exception.ItemNotFoundException;
import com.ecomdev.repository.PaymentRepository;
import com.ecomdev.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
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
                .orElseThrow(() -> new ItemNotFoundException("O'quvchi topilmadi. ID: " + dto.getStudentId()));

        // 1. To'lov sanasidan qaysi oy ekanligini va oyning boshlanish/tugash kunlarini aniqlaymiz
        YearMonth yearMonth = YearMonth.from(dto.getPaidDate());
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();


        // 2. Shu oyda o'quvchi allaqachon jami qancha to'laganini hisoblaymiz
        BigDecimal alreadyPaid = paymentRepository.findByStudentIdAndPaidDateBetween(student.getId(), startDate, endDate)
                .stream()
                .map(PaymentEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Shu oy uchun qolgan haqiqiy qarz summasi
        BigDecimal remainingDebt = student.getMonthlyFee().subtract(alreadyPaid);
        // Tekshiruv A: O'quvchi allaqachon to'liq to'lab bo'lgan bo'lsa
        if (remainingDebt.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppBadRequestException(
                    "Ushbu o'quvchi " + yearMonth + " oyi uchun to'lovni allaqachon to'liq amalga oshirgan."
            );
        }

        // Tekshiruv B: Kiritilayotgan summa qolgan qarzdan katta bo'lsa
        if (dto.getAmount().compareTo(remainingDebt) > 0) {
            throw new AppBadRequestException(
                    "To'lov summasi qolgan qarzdan ko'p bo'lishi mumkin emas. Ushbu oy uchun qolgan qarz: " + remainingDebt + " so'm."
            );
        }

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
            throw new ItemNotFoundException("O'quvchi topilmadi. ID: " + studentId);
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