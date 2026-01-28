package dev.suel.mstransactionapi.infra.services;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionapi.application.gateway.TransactionEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTransactionEventPublisher implements TransactionEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaTransactionEventPublisher.class);
    private static final String TOPIC = "transaction.requested";
    private final KafkaTemplate<String, TransactionKafkaEventData> kafkaTemplate;

    public KafkaTransactionEventPublisher(KafkaTemplate<String, TransactionKafkaEventData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(TransactionKafkaEventData event) {
        kafkaTemplate.send(TOPIC, event.transactionId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Falha ao publicar evento Kafka. transactionId={}", event.transactionId(), ex);
                    }
                });
    }
}
