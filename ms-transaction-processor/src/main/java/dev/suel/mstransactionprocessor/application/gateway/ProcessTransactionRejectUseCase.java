package dev.suel.mstransactionprocessor.application.gateway;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;

public interface ProcessTransactionRejectUseCase {
    void execute(TransactionKafkaEventData event, String message);
}
