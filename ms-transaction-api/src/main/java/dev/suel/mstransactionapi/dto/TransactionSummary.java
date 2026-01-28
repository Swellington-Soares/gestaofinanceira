package dev.suel.mstransactionapi.dto;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionStatus;

import java.math.BigDecimal;

public record TransactionSummary (
        OperationType operationType,
        TransactionStatus status,
        CurrencyType currency,
        Long totalTransactions,
        BigDecimal totalAmount
){
}
