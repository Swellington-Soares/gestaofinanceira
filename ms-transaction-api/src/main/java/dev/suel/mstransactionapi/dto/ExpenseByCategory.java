package dev.suel.mstransactionapi.dto;

import dev.suel.gestaofinanceira.types.OperationType;

import java.math.BigDecimal;

public record ExpenseByCategory(
        OperationType category,
        BigDecimal totalAmount
){
}
