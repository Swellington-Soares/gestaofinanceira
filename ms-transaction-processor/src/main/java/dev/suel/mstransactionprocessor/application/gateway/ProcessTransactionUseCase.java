package dev.suel.mstransactionprocessor.application.gateway;


import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;

public interface ProcessTransactionUseCase {
    void execute(TransactionKafkaEventData event);
}
