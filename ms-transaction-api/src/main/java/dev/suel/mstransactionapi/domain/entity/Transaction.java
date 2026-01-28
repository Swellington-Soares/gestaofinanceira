package dev.suel.mstransactionapi.domain.entity;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class Transaction {
    private UUID transactionId;
    private LocalDateTime createdDate;
    private LocalDateTime processedDate;
    private BigDecimal amount = new BigDecimal("0.0");
    private BigDecimal exchange = new BigDecimal("1.0");
    private OperationType operationType;
    private TransactionStatus status = TransactionStatus.PENDING;
    private String message;
    private Long userId;
    private CurrencyType currencyType;
    private Long destAccountId;


    public Transaction(UUID transactionId,
                       LocalDateTime createdDate,
                       LocalDateTime processedDate,
                       BigDecimal amount,
                       BigDecimal exchange,
                       OperationType operationType,
                       TransactionStatus status,
                       String message,
                       Long userId,
                       CurrencyType currencyType,
                       Long destAccountId) {
        this.transactionId = transactionId;
        this.createdDate = createdDate;
        this.processedDate = processedDate;
        this.amount = amount;
        this.exchange = exchange;
        this.operationType = operationType;
        this.status = status;
        this.message = message;
        this.userId = userId;
        this.currencyType = currencyType;
        this.destAccountId = destAccountId;
    }

    public Transaction() {
        this.exchange = new BigDecimal("1.0");
        this.amount = new BigDecimal("0");
    }

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    public static TransactionBuilder withId() {
        return new TransactionBuilder().transactionId(UUID.randomUUID());
    }

    public Long getDestAccountId() {
        return destAccountId;
    }

    public Transaction setDestAccountId(Long destAccountId) {
        this.destAccountId = destAccountId;
        return this;
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

    public BigDecimal getFinalAmount() {
        if (exchange == null)
            exchange = new BigDecimal("1.0");
        if (amount == null )
            amount = new BigDecimal("0.0");
        return exchange.multiply(amount);
    }

    public boolean isOwner(Long ownerId) {
        return userId != null && userId.equals(ownerId);
    }

    public void approve(String message) {
        this.message = message;
        this.status = TransactionStatus.APPROVED;
        this.processedDate = LocalDateTime.now();
    }

    public void approve() {
        approve("Transação aprovada.");
    }

    public void reject(String message) {
        this.message = message;
        this.status = TransactionStatus.REJECTED;
        this.processedDate = LocalDateTime.now();
    }


    public void reject() {
        approve("Transação rejeitada.");
    }

    public static class TransactionBuilder {
        private UUID transactionId;
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

        public TransactionBuilder currencyType(CurrencyType currencyType) {
            this.currencyType = currencyType;
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


        public TransactionBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public TransactionBuilder destAccountId(Long destAccountId) {
            this.destAccountId = destAccountId;
            return this;
        }

        public Transaction build() {
            return new Transaction(
                    transactionId,
                    createdDate,
                    processedDate,
                    amount,
                    exchange,
                    operationType,
                    status,
                    message,
                    userId,
                    currencyType,
                    destAccountId
            );
        }
    }
}
