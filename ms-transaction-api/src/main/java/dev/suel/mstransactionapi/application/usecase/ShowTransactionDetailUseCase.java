package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.exception.AccessDeniedException;
import dev.suel.mstransactionapi.application.exception.ResourceNotFoundException;
import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionDetailResponse;
import dev.suel.mstransactionapi.infra.mapper.TransactionMapper;

import java.util.UUID;

public class ShowTransactionDetailUseCase {

    private final TransactionServicePort transactionServicePort;

    public ShowTransactionDetailUseCase(TransactionServicePort transactionServicePort) {
        this.transactionServicePort = transactionServicePort;
    }

    public TransactionDetailResponse execute(UUID transactionId, Long ownerId){
        Transaction transactionModel = transactionServicePort.getById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("A transação com ID: " + transactionId+ " não existe."));

        if (!transactionModel.isOwner(ownerId))
            throw new AccessDeniedException("Você não pode realizar esta ação.");

        return new TransactionMapper().modelToResponse(transactionModel);

    }
}
