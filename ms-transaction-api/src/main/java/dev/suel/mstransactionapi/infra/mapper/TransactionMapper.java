package dev.suel.mstransactionapi.infra.mapper;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionDetailResponse;
import dev.suel.mstransactionapi.infra.persistence.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionDetailResponse modelToResponse(Transaction transaction) {
        return new TransactionDetailResponse(
                transaction.getUserId(),
                transaction.getCreatedDate(),
                transaction.getProcessedDate(),
                transaction.getTransactionId(),
                transaction.getAmount(),
                transaction.getExchange(),
                transaction.getOperationType(),
                transaction.getStatus(),
                transaction.getFinalAmount(),
                transaction.getCurrencyType()
        );
    }

    public Transaction transactionEntityToModel(TransactionEntity entity) {
        return Transaction.builder()
                .userId(entity.getUserId())
                .createdDate(entity.getCreatedDate())
                .processedDate(entity.getProcessedDate())
                .transactionId(entity.getTransactionId())
                .amount(entity.getAmount())
                .exchange(entity.getExchange())
                .currencyType(entity.getCurrencyType())
                .operationType(entity.getOperationType())
                .status(entity.getStatus())
                .message(entity.getMessage())
                .build();
    }

    public TransactionEntity modelToEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .transactionId(transaction.getTransactionId())
                .createdDate(transaction.getCreatedDate())
                .processedDate(transaction.getProcessedDate())
                .amount(transaction.getAmount())
                .exchange(transaction.getExchange())
                .operationType(transaction.getOperationType())
                .status(transaction.getStatus())
                .message(transaction.getMessage())
                .currencyType(transaction.getCurrencyType())
                .userId(transaction.getUserId())
                .build();
    }

    public TransactionKafkaEventData modelToKafkaData(Transaction model) {
        return new TransactionKafkaEventData(
                model.getTransactionId(),
                model.getUserId(),
                model.getCurrencyType(),
                model.getAmount(),
                model.getDestAccountId()
        );
    }
}
