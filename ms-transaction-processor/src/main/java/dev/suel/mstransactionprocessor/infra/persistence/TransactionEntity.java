package dev.suel.mstransactionprocessor.infra.persistence;

import dev.suel.mstransactionprocessor.domain.CurrencyType;
import dev.suel.mstransactionprocessor.domain.OperationType;
import dev.suel.mstransactionprocessor.domain.TransactionStatus;
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
    @Builder.Default
    private BigDecimal amount = new BigDecimal("0");

    @Builder.Default
    private BigDecimal exchange = new BigDecimal("1.0");

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PENDING;

    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType currencyType;

    @Column(nullable = false)
    private Long userId;

    private Long destAccount;
}
