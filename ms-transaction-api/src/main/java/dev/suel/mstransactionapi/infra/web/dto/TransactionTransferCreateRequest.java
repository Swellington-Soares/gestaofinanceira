package dev.suel.mstransactionapi.infra.web.dto;

import java.math.BigDecimal;

public record TransactionTransferCreateRequest(
        BigDecimal amount,
        Long destAccount
) {
}
