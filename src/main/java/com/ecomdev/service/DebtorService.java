package com.ecomdev.service;

import com.ecomdev.dto.general.DebtorResponseDTO;
import com.ecomdev.entity.PaymentEntity;
import com.ecomdev.entity.StudentEntity;
import com.ecomdev.repository.PaymentRepository;
import com.ecomdev.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DebtorService {

    private final StudentRepository studentRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public List<DebtorResponseDTO> getDebtorsByMonth(String monthStr) {
        YearMonth yearMonth;
        try {
            // "2026-10" kabi formatni o'qiydi
            yearMonth = YearMonth.parse(monthStr);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("Noto'g'ri oy formati. To'g'ri format: YYYY-MM (Masalan: 2026-10)");
        }

        // Shu oyning 1-kuni va oxirgi kuni
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 1. Barcha o'quvchilarni olamiz
        List<StudentEntity> students = studentRepository.findAll();

        // 2. Shu oyda bo'lgan to'lovlarni olamiz
        List<PaymentEntity> payments = paymentRepository.findByPaidDateBetween(startDate, endDate);

        // 3. To'lovlarni student_id bo'yicha guruhlab, har birining to'lagan summasini yig'amiz (SUM)
        Map<Long, BigDecimal> studentPaidMap = payments.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getStudent().getId(),
                        Collectors.reducing(BigDecimal.ZERO, PaymentEntity::getAmount, BigDecimal::add)
                ));

        List<DebtorResponseDTO> debtors = new ArrayList<>();

        // 4. Har bir o'quvchini tekshiramiz
        for (StudentEntity student : students) {
            BigDecimal totalPaid = studentPaidMap.getOrDefault(student.getId(), BigDecimal.ZERO);
            BigDecimal monthlyFee = student.getMonthlyFee();

            // Agar to'lagan summasi oylik to'lovidan kam bo'lsa (totalPaid < monthlyFee)
            if (totalPaid.compareTo(monthlyFee) < 0) {
                BigDecimal debtAmount = monthlyFee.subtract(totalPaid);

                debtors.add(DebtorResponseDTO.builder()
                        .studentId(student.getId())
                        .fullName(student.getFullName())
                        .phoneNumber(student.getPhoneNumber())
                        .groupName(student.getGroupName())
                        .monthlyFee(monthlyFee)
                        .totalPaid(totalPaid)
                        .debtAmount(debtAmount)
                        .build());
            }
        }

        return debtors;
    }
}