package dev.suel.mstransactionapi.dto;

import dev.suel.mstransactionapi.domain.CurrencyType;
import dev.suel.mstransactionapi.domain.OperationType;
import dev.suel.mstransactionapi.domain.TransactionStatus;

import java.time.LocalDateTime;

public record TransactionReportFilter(
        LocalDateTime startDate,
        LocalDateTime endDate,
        OperationType operationType,
        TransactionStatus status,
        CurrencyType currency,
        Long userId
) {
}
