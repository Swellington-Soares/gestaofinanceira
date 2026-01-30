package dev.suel.mstransactionapi.application.usecase;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionStatus;
import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;
import dev.suel.mstransactionapi.infra.web.dto.TransactionWithdrawCreateRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateWithDrawTransactionUseCase {

    private final TransactionServicePort transactionServicePort;

    public CreateWithDrawTransactionUseCase(TransactionServicePort transactionServicePort) {
        this.transactionServicePort = transactionServicePort;
    }

    public TransactionCreatedResponseDto execute(Long ownerId, TransactionWithdrawCreateRequest data) {

        if (ownerId == null)
            throw new IllegalArgumentException("Id do solicitante não foi informado.");

        if (data == null || data.amount() == null || data.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("O valor da transação inválido.");

        Transaction transactionModel = Transaction.withId()
                .createdDate(LocalDateTime.now())
                .operationType(OperationType.WITHDRAW)
                .amount(data.amount())
                .userId(ownerId)
                .status(TransactionStatus.PENDING)
                .currencyType(CurrencyType.Default())
                .build();

       return transactionServicePort.save(transactionModel);
    }
}
