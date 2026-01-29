package dev.suel.mstransactionapi.application.usecase;

import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionStatus;
import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;
import dev.suel.mstransactionapi.infra.web.dto.TransactionCustomCreateRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CreateCustomTransactionUseCase {

    private final TransactionServicePort transactionServicePort;

    public CreateCustomTransactionUseCase(TransactionServicePort transactionServicePort) {
        this.transactionServicePort = transactionServicePort;
    }

    private final List<OperationType> allowedCustomOperations = List.of(
            OperationType.EXTERNAL,
            OperationType.CREDIT_CARD_PURCHASE,
            OperationType.PURCHASER
    );

    public TransactionCreatedResponseDto execute(Long ownerId, TransactionCustomCreateRequest data) {

        if (ownerId == null)
            throw new IllegalArgumentException("Id do solicitante não foi informado.");

        if (data == null || data.amount() == null || data.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("O valor da transação inválido.");

        if (data.operationType() == null || !allowedCustomOperations.contains(data.operationType()))
            throw new IllegalArgumentException("Apenas operações de " +
                    allowedCustomOperations.stream()
                            .map(OperationType::toString)
                            .collect(Collectors.joining())
                    + " são permitidas." );

        Transaction transactionModel = Transaction.withId()
                .createdDate(LocalDateTime.now())
                .operationType(data.operationType())
                .amount(data.amount())
                .userId(ownerId)
                .status(TransactionStatus.PENDING)
                .currencyType(data.currencyType())
                .build();

        return transactionServicePort.save(transactionModel);
    }
}
