package dev.suel.mstransactionapi.application.usecase;


import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.dto.TransactionDetailResponse;
import dev.suel.mstransactionapi.infra.mapper.TransactionMapper;

public class GetAllTransactionUseCase {

    private final TransactionServicePort transactionServicePort;

    public GetAllTransactionUseCase(TransactionServicePort transactionServicePort) {
        this.transactionServicePort = transactionServicePort;
    }

    public PaginatedResponse<TransactionDetailResponse> execute(PageDataDomain pageDataDomain, Long ownerId) {
        return transactionServicePort.findAllByOwnerId(ownerId, pageDataDomain)
                .map(c -> new TransactionMapper().modelToResponse(c));
    }
}
