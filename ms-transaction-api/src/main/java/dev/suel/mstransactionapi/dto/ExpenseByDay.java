package dev.suel.mstransactionapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseByDay(
        LocalDateTime date,
        BigDecimal totalAmount
) {
}
