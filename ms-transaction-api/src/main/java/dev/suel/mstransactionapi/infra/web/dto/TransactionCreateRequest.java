package dev.suel.mstransactionapi.infra.web.dto;


import dev.suel.mstransactionapi.domain.CurrencyType;
import dev.suel.mstransactionapi.domain.OperationType;

import java.math.BigDecimal;

public record TransactionCreateRequest(

        BigDecimal amount,

        OperationType operationType,

        CurrencyType currencyType
) {
}
