package dev.suel.mstransactionapi.application.gateway;


import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;

public interface TransactionEventPublisher {
    void publish(TransactionKafkaEventData event);
}
