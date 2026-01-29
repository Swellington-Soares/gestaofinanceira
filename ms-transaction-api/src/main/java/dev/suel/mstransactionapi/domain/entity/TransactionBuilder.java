package dev.suel.mstransactionapi.domain.entity;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TransactionBuilder {
    private UUID id;
    private LocalDateTime createdDate;
    private LocalDateTime processedDate;
    private BigDecimal amount = new BigDecimal("0.0");
    private BigDecimal exchange = new BigDecimal("1.0");
    private OperationType operationType;
    private TransactionStatus status = TransactionStatus.PENDING;
    private String message;
    private CurrencyType currencyType;
    private Long userId;
    private Long destAccountId;

    public TransactionBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public TransactionBuilder createdDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public TransactionBuilder processedDate(LocalDateTime processedDate) {
        this.processedDate = processedDate;
        return this;
    }

    public TransactionBuilder amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransactionBuilder exchange(BigDecimal exchange) {
        this.exchange = exchange;
        return this;
    }

    public TransactionBuilder operationType(OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public TransactionBuilder status(TransactionStatus status) {
        this.status = status;
        return this;
    }

    public TransactionBuilder message(String message) {
        this.message = message;
        return this;
    }

    public TransactionBuilder currencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
        return this;
    }

    public TransactionBuilder userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public TransactionBuilder destAccountId(Long destAccountId) {
        this.destAccountId = destAccountId;
        return this;
    }

    public Transaction build() {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setUserId(userId);
        transaction.setCreatedDate(createdDate);
        transaction.setProcessedDate(processedDate);
        transaction.setAmount(amount);
        transaction.setExchange(exchange);
        transaction.setOperationType(operationType);
        transaction.setStatus(status);
        transaction.setMessage(message);
        transaction.setCurrencyType(currencyType);
        transaction.setDestAccountId(destAccountId);
        return transaction;
    }
}
