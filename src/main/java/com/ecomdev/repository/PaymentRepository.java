package com.ecomdev.repository;

import com.ecomdev.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByStudentIdOrderByPaidDateDesc(Long studentId);

    List<PaymentEntity> findByPaidDateBetween(LocalDate startDate, LocalDate endDate);

    // O'quvchining berilgan oraliqdagi (oydagi) barcha to'lovlari
    List<PaymentEntity> findByStudentIdAndPaidDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);
}