package dev.suel.mstransactionapi.dto;

import dev.suel.mstransactionapi.domain.OperationType;

import java.math.BigDecimal;

public record ExpenseByCategory(
        OperationType category,
        BigDecimal totalAmount
) {
}
