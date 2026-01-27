package dev.suel.mstransactionapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.suel.mstransactionapi.domain.CurrencyType;

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
