package com.ecomdev.controller;

import com.ecomdev.dto.general.DebtorResponseDTO;
import com.ecomdev.service.DebtorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/debtors", "/debtors"})
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DebtorController {

    private final DebtorService debtorService;

    // GET /debtors?month=2026-10
    @GetMapping
    public ResponseEntity<List<DebtorResponseDTO>> getDebtors(@RequestParam("month") String month) {
        return ResponseEntity.ok(debtorService.getDebtorsByMonth(month));
    }
}