package dev.suel.mstransactionprocessor.infra.kafka;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionRejectUseCase;
import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionUseCase;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionRequestedConsumer {

    private final ProcessTransactionUseCase processTransactionUseCase;
    private final ProcessTransactionRejectUseCase processTransactionRejectUseCase;

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 5000),
            include = {
                    FeignException.class,
                    RuntimeException.class
            }
    )
    @KafkaListener(
            topics = "transaction.requested",
            groupId = "transaction-processor"
    )
    public void consume(TransactionKafkaEventData event) {
        processTransactionUseCase.execute(event);
    }

    @KafkaListener(
            topics = "transaction.requested.DLT",
            groupId = "transaction-processor-dlt"
    )
    public void consumeDlq(
            TransactionKafkaEventData event,
            @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String errorMessage
    ) {
        processTransactionRejectUseCase.execute(event, errorMessage);
    }


}
