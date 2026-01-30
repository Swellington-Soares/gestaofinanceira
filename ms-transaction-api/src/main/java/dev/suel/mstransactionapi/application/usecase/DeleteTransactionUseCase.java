package dev.suel.mstransactionapi.application.usecase;


import dev.suel.gestaofinanceira.types.TransactionStatus;
import dev.suel.mstransactionapi.application.exception.AccessDeniedException;
import dev.suel.mstransactionapi.application.exception.InvalidOperationException;
import dev.suel.mstransactionapi.application.exception.ResourceNotFoundException;
import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.entity.Transaction;

import java.util.UUID;

public class DeleteTransactionUseCase {

    private final TransactionServicePort transactionServicePort;

    public DeleteTransactionUseCase(TransactionServicePort transactionServicePort) {
        this.transactionServicePort = transactionServicePort;
    }

    public void execute(UUID transactionId, Long ownerId) {
        Transaction transactionModel = transactionServicePort.getById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("A transação com ID: " + transactionId+ " não existe."));

        if (!transactionModel.isOwner(ownerId))
            throw new AccessDeniedException("Você não pode realizar esta ação.");

        if (transactionModel.getStatus().equals(TransactionStatus.PENDING))
            throw new InvalidOperationException("Esta operação não pode ser executada no momento. Transação indisponível.");

        transactionServicePort.deleteTransaction(transactionId);
    }
}
