package dev.suel.mstransactionapi.dto;


import dev.suel.gestaofinanceira.types.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionCreatedResponseDto(
        UUID id,
        TransactionStatus status,
        LocalDateTime createdAt
) {
}
