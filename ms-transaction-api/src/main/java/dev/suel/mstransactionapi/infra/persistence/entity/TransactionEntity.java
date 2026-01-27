package dev.suel.mstransactionapi.infra.persistence.entity;

import dev.suel.mstransactionapi.domain.CurrencyType;
import dev.suel.mstransactionapi.domain.OperationType;
import dev.suel.mstransactionapi.domain.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    private UUID transactionId;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime processedDate;

    @Column(nullable = false)
    private BigDecimal amount;
    private BigDecimal exchange = new BigDecimal("1.0");

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PENDING;

    private String message;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;
    private Long userId;

    @Column()
    private Long destAccount;
}
