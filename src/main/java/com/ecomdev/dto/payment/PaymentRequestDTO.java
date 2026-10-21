package com.ecomdev.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    @NotNull(message = "O'quvchi tanlanishi shart")
    private Long studentId;

    @NotNull(message = "To'lov summasi kiritilishi shart")
    @Positive(message = "To'lov summasi 0 dan katta bo'lishi kerak")
    private BigDecimal amount;

    @NotNull(message = "To'lov sanasi kiritilishi shart")
    private LocalDate paidDate;
}