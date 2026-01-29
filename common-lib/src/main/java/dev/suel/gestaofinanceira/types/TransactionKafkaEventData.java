package dev.suel.gestaofinanceira.types;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
        UUID id,
        Long userId,
        CurrencyType currencyType,
        BigDecimal amount,
        Long destAccount
        ) {
}
