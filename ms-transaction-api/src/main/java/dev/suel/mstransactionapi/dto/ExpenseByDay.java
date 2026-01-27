package dev.suel.mstransactionapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExpenseByDay(
        Object date,
        BigDecimal totalAmount
) {
}
