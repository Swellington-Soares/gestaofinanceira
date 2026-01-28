package dev.suel.mstransactionprocessor.infra.external.mockapi;

import java.math.BigDecimal;

public record UserBalanceUpdate(
        BigDecimal balance
) {
}
