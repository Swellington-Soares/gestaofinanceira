package dev.suel.mstransactionapi.infra.web.dto;

import java.math.BigDecimal;

public record TransactionWithdrawCreateRequest(
        BigDecimal amount
) {
}
