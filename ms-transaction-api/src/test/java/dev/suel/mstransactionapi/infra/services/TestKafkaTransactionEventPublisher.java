package dev.suel.mstransactionapi.infra.services;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

class TestKafkaTransactionEventPublisher {

    private KafkaTemplate<String, TransactionKafkaEventData> kafkaTemplate;
    private KafkaTransactionEventPublisher publisher;

    @BeforeEach
    void setup() {
        kafkaTemplate = mock(KafkaTemplate.class);
        publisher = new KafkaTransactionEventPublisher(kafkaTemplate);
    }

    @Test
    void shouldPublishEventToKafka() {
        TransactionKafkaEventData event = new TransactionKafkaEventData(
                UUID.randomUUID(),
                10L,
                CurrencyType.Default(),
                new BigDecimal("50.00"),
                99L
        );

        given(kafkaTemplate.send(anyString(), anyString(), any(TransactionKafkaEventData.class)))
                .willReturn(new CompletableFuture<>());

        publisher.publish(event);

        then(kafkaTemplate).should()
                .send("transaction.requested", event.transactionId().toString(), event);
    }
}
