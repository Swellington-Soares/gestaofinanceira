package dev.suel.mstransactionapi.dto;

import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDetailResponse(
         Long userId,
         LocalDateTime createdDate,
         LocalDateTime processedDate,
         UUID transactionId,
         BigDecimal amount,
         BigDecimal exchange,
         OperationType operationType,
         TransactionStatus status,
         BigDecimal finalAmount
) {
}
