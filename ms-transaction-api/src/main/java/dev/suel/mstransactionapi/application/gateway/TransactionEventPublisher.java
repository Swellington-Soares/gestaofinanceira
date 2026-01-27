package dev.suel.mstransactionapi.application.gateway;

import dev.suel.mstransactionapi.dto.TransactionKafkaEventData;

public interface TransactionEventPublisher {
    void publish(TransactionKafkaEventData event);
}
