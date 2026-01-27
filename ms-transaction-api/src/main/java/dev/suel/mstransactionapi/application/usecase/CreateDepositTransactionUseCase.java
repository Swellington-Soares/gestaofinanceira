package dev.suel.mstransactionapi.application.usecase;


import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.OperationType;
import dev.suel.mstransactionapi.domain.TransactionStatus;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;
import dev.suel.mstransactionapi.infra.web.dto.TransactionCreateRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateDepositTransactionUseCase {

    private final TransactionServicePort transactionServicePort;

    public CreateDepositTransactionUseCase(TransactionServicePort transactionServicePort) {
        this.transactionServicePort = transactionServicePort;
    }

    public TransactionCreatedResponseDto execute(Long ownerId,
                                                 TransactionCreateRequest data){
        if (ownerId == null)
            throw new IllegalArgumentException("Id do solicitante não foi informado.");

        if (data == null || data.amount() == null || data.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("O valor da transação inválido.");

        Transaction transactionModel = Transaction.withId()
                .createdDate(LocalDateTime.now())
                .operationType(OperationType.DEPOSIT)
                .amount(data.amount())
                .userId(ownerId)
                .status(TransactionStatus.PENDING)
                .currencyType(data.currencyType())
                .build();

        return transactionServicePort.save(transactionModel);

    }
}
