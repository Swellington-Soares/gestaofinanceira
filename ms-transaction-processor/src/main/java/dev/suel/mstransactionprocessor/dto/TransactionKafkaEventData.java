package dev.suel.mstransactionprocessor.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.suel.mstransactionprocessor.domain.CurrencyType;


import java.math.BigDecimal;
import java.util.UUID;

@JsonPropertyOrder({
        "transactionId",
        "userId",
        "currencyType",
        "amount",
        "destAccount"
})
public record TransactionKafkaEventData(
        UUID transactionId,
        Long userId,
        CurrencyType currencyType,
        BigDecimal amount,
        Long destAccount
        ) {
}
