package dev.suel.mstransactionapi.infra.web.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionTransferCreateRequest(
        @Positive(message = "O valor precisa ser maior que zero.")
        BigDecimal amount,
        Long destAccount
) {
}
