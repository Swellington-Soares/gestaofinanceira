package dev.suel.mstransactionprocessor.infra.mapper;


import dev.suel.mstransactionprocessor.domain.entity.Transaction;
import dev.suel.mstransactionprocessor.infra.persistence.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

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
}
