package dev.suel.mstransactionprocessor.infra.kafka;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionRejectUseCase;
import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionUseCase;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.DltHandler;
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
                    FeignException.class
            },
            dltTopicSuffix = ".dlt"
    )
    @KafkaListener(
            topics = "transaction.requested",
            groupId = "transaction-processor"
    )
    public void consume(TransactionKafkaEventData event) {
        processTransactionUseCase.execute(event);
    }


    @DltHandler
    public void consumeDlq(
            TransactionKafkaEventData event,
            @Header(value = KafkaHeaders.DLT_EXCEPTION_MESSAGE, required = false) String errorMessage
    ) {
        processTransactionRejectUseCase.execute(event, errorMessage);
    }


}
