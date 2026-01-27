package dev.suel.mstransactionapi.application.usecase;


import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.CurrencyType;
import dev.suel.mstransactionapi.domain.OperationType;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;
import dev.suel.mstransactionapi.infra.web.dto.TransactionTransferCreateRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateTransferTransactionUseCase {

    private final TransactionServicePort transactionServicePort;

    public CreateTransferTransactionUseCase(TransactionServicePort transactionServicePort) {
        this.transactionServicePort = transactionServicePort;
    }

    public TransactionCreatedResponseDto execute(Long id, TransactionTransferCreateRequest data) {

        if (id == null)
            throw new IllegalArgumentException("Id do solicitante não foi informado.");

        if (data == null || data.amount() == null || data.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("O valor da transação é inválido.");

        Transaction transactionModel = Transaction.withId()
                .createdDate(LocalDateTime.now())
                .operationType(OperationType.TRANSFER)
                .amount(data.amount())
                .userId(id)
                .currencyType(CurrencyType.Default())
                .build();

        transactionServicePort.save(transactionModel);

        return new TransactionCreatedResponseDto(
                transactionModel.getTransactionId(),
                transactionModel.getStatus(),
                transactionModel.getCreatedDate()
        );

    }
}
