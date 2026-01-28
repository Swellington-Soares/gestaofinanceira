package dev.suel.mstransactionapi.infra.web.dto;


import dev.suel.gestaofinanceira.types.CurrencyType;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionCreateRequest(
        @Positive(message = "O valor precisa ser maior que zero.")
        BigDecimal amount,
        CurrencyType currencyType
) {
}
