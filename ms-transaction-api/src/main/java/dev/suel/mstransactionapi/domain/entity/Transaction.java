package dev.suel.mstransactionapi.domain.entity;

import dev.suel.mstransactionapi.domain.CurrencyType;
import dev.suel.mstransactionapi.domain.OperationType;
import dev.suel.mstransactionapi.domain.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID transactionId;
    private LocalDateTime createdDate;
    private LocalDateTime processedDate;
    private BigDecimal amount;
    private BigDecimal exchange;
    private OperationType operationType;
    private TransactionStatus status = TransactionStatus.PENDING;
    private String message;
    private CurrencyType currencyType;
    private Long userId;

    public Transaction(UUID transactionId, LocalDateTime createdDate, LocalDateTime processedDate, BigDecimal amount, BigDecimal exchange, OperationType operationType, TransactionStatus status, String message, CurrencyType currencyType, Long userId) {
        this.transactionId = transactionId;
        this.createdDate = createdDate;
        this.processedDate = processedDate;
        this.amount = amount;
        this.exchange = exchange;
        this.operationType = operationType;
        this.status = status;
        this.message = message;
        this.currencyType = currencyType;
        this.userId = userId;
    }

    public Transaction() {
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Transaction setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Transaction setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    public Transaction setProcessedDate(LocalDateTime processedDate) {
        this.processedDate = processedDate;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Transaction setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public BigDecimal getExchange() {
        return exchange;
    }

    public Transaction setExchange(BigDecimal exchange) {
        this.exchange = exchange;
        return this;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Transaction setOperationType(OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Transaction setStatus(TransactionStatus status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Transaction setMessage(String message) {
        this.message = message;
        return this;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public Transaction setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Transaction setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    public static TransactionBuilder withId(){
        return new TransactionBuilder().transactionId( UUID.randomUUID() );
    }

    public BigDecimal getFinalAmount() {
        return exchange.multiply(amount);
    }

    public boolean isOwner(Long ownerId) {
        return userId != null && userId.equals(ownerId);
    }

    public static class TransactionBuilder{
        private UUID transactionId;
        private LocalDateTime createdDate;
        private LocalDateTime processedDate;
        private BigDecimal amount;
        private BigDecimal exchange;
        private OperationType operationType;
        private TransactionStatus status = TransactionStatus.PENDING;
        private String message;
        private CurrencyType currencyType;
        private Long userId;

        public TransactionBuilder transactionId(UUID transactionId) {
            this.transactionId = transactionId;
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

        public Transaction build(){
            return new Transaction(transactionId,
                    createdDate, processedDate,
                    amount, exchange, operationType,
                    status, message, currencyType, userId);
        }
    }
}
