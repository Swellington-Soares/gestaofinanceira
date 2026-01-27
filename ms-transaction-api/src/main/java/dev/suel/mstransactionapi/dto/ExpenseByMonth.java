package dev.suel.mstransactionapi.dto;

import java.math.BigDecimal;

public record ExpenseByMonth(
        Integer year,
        Integer month,
        BigDecimal totalAmount
) {
}
