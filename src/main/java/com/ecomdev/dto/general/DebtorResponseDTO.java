package com.ecomdev.dto.general;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebtorResponseDTO {
    private Long studentId;
    private String fullName;
    private String phoneNumber;
    private String groupName;
    private BigDecimal monthlyFee; // Oylik to'lashi kerak bo'lgan summa
    private BigDecimal totalPaid;  // Shu oyda jami qancha to'lagani
    private BigDecimal debtAmount; // Qancha qarzi qolgani (monthlyFee - totalPaid)
}