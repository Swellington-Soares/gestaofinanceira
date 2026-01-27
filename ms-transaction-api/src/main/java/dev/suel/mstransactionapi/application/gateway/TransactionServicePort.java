package dev.suel.mstransactionapi.application.gateway;


import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface TransactionServicePort {
    TransactionCreatedResponseDto save(Transaction model);
    PaginatedResponse<Transaction> findAllByOwnerId(Long ownerId, PageDataDomain pageDataDomain);
    Optional<Transaction> getById(UUID transactionId);
    void deleteTransaction(UUID transactionId);
}
