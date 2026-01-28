package dev.suel.mstransactionprocessor.infra.kafka;

import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionUseCase;
import dev.suel.mstransactionprocessor.dto.TransactionKafkaEventData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionRequestedConsumer {
    private static final Logger log = LoggerFactory.getLogger(TransactionRequestedConsumer.class);

    private final ProcessTransactionUseCase processTransactionUseCase;

    @KafkaListener(
            topics = "transaction.requested",
            groupId = "transaction-consumer-group"
    )
    public void consume(
            @Payload TransactionKafkaEventData event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {

        log.info("""
                Evento recebido:
                key={}
                offset={}
                payload={}
                """, key, offset, event);

        processTransactionUseCase.execute(event);


    }


}
