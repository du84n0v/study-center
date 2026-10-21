package com.ecomdev.dto.payment;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private BigDecimal amount;
    private LocalDate paidDate;
    private LocalDateTime createdAt;
}