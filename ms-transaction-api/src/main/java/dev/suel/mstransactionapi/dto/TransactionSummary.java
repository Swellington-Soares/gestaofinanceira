package dev.suel.mstransactionapi.dto;

import dev.suel.mstransactionapi.domain.CurrencyType;
import dev.suel.mstransactionapi.domain.OperationType;
import dev.suel.mstransactionapi.domain.TransactionStatus;

import java.math.BigDecimal;

public record TransactionSummary (
        OperationType operationType,
        TransactionStatus status,
        CurrencyType currency,
        Long totalTransactions,
        BigDecimal totalAmount
){
}
