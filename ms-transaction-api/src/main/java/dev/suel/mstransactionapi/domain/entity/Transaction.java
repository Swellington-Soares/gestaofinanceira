package dev.suel.mstransactionapi.domain.entity;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Transaction {
    private UUID id;
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


    public Transaction(UUID id,
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
        this.id = id;
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


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(LocalDateTime processedDate) {
        this.processedDate = processedDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getExchange() {
        return exchange;
    }

    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }

    public Long getDestAccountId() {
        return destAccountId;
    }

    public void setDestAccountId(Long destAccountId) {
        this.destAccountId = destAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    //OPERAÇÕES

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

    public boolean isPending() {
        return this.status == TransactionStatus.PENDING;
    }


    public void approve(String message) {
        this.message = message;
        this.status = TransactionStatus.APPROVED;
        this.processedDate = LocalDateTime.now();
    }

    public void approve() {
        approve("Transação realizada com sucesso.");
    }

    public void reject(String message) {
        this.message = message;
        this.status = TransactionStatus.REJECTED;
        this.processedDate = LocalDateTime.now();
    }


    public void reject() {
        reject("Transação rejeitada.");
    }


    //BUILDER MODE

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    public static TransactionBuilder withId(){
        return new TransactionBuilder().id(UUID.randomUUID());
    }
}
