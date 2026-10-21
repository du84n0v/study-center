package com.ecomdev.dto.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequestDTO {

    @NotBlank(message = "O'quvchi ismi bo'sh bo'lishi mumkin emas")
    private String fullName;

    @NotBlank(message = "Telefon raqami kiritilishi shart")
    private String phoneNumber;

    @NotBlank(message = "Guruh nomi bo'sh bo'lishi mumkin emas")
    private String groupName;

    @NotNull(message = "Oylik to'lov summasi kiritilishi shart")
    @Positive(message = "Oylik to'lov 0 dan katta bo'lishi kerak")
    private BigDecimal monthlyFee;
}