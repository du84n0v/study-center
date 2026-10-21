package com.ecomdev.dto.student;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDTO {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String groupName;
    private BigDecimal monthlyFee;
    private LocalDateTime createdAt;
}