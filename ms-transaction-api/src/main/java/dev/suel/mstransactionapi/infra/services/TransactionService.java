package dev.suel.mstransactionapi.infra.services;

import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.infra.persistence.repository.TransactionEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService implements TransactionServicePort {

    private final TransactionEntityRepository transactionRepository;

    @Override
    public void save(Transaction model) {

    }

    @Override
    public PaginatedResponse<Transaction> findAllByOwnerId(Long ownerId, PageDataDomain pageDataDomain) {
        return null;
    }

    @Override
    public Optional<Transaction> getById(UUID transactionId) {
        return Optional.empty();
    }

    @Override
    public void deleteTransaction(UUID transactionId) {

    }

}
