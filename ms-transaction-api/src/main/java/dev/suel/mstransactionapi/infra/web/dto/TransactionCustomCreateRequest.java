package dev.suel.mstransactionapi.infra.web.dto;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.gestaofinanceira.types.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionCustomCreateRequest(
        @Positive(message = "O valor precisa ser maior que zero.")
        BigDecimal amount,

        @NotNull(message = "O tipo da moeda é obrigatório.")
        CurrencyType currencyType,

        @NotNull(message = "O tipo da transação é obrigatório.")
        OperationType operationType
) {
}
