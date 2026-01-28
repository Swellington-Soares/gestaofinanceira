package dev.suel.mstransactionprocessor.application.gateway;

import dev.suel.mstransactionprocessor.dto.TransactionKafkaEventData;

public interface ProcessTransactionUseCase {
    void execute(TransactionKafkaEventData event);
}
