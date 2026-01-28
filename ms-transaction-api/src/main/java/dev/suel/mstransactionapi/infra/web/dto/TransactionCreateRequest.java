package dev.suel.mstransactionapi.infra.web.dto;


import dev.suel.mstransactionapi.domain.CurrencyType;
import dev.suel.mstransactionapi.domain.OperationType;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionCreateRequest(
        @Positive(message = "O valor precisa ser maior que zero.")
        BigDecimal amount,
        CurrencyType currencyType
) {
}
