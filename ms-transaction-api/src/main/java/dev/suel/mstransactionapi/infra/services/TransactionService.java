package dev.suel.mstransactionapi.infra.services;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionapi.application.gateway.TransactionEventPublisher;
import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;
import dev.suel.mstransactionapi.infra.mapper.PageMapper;
import dev.suel.mstransactionapi.infra.mapper.PageSortMapper;
import dev.suel.mstransactionapi.infra.mapper.TransactionMapper;
import dev.suel.mstransactionapi.infra.persistence.TransactionEntity;
import dev.suel.mstransactionapi.infra.persistence.TransactionEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TransactionService implements TransactionServicePort {

    private final TransactionEntityRepository transactionRepository;
    private final TransactionMapper mapper;
    private final TransactionEventPublisher eventPublisher;
    private final PageSortMapper pageSortMapper;
    private final PageMapper pageMapper;

    @Transactional
    public TransactionCreatedResponseDto save(Transaction model) {

        TransactionEntity entity = mapper.modelToEntity(model);
        transactionRepository.save(entity);

        TransactionKafkaEventData event = mapper.modelToKafkaData(model);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        CompletableFuture.runAsync(() -> eventPublisher.publish(event));
                    }
                }
        );

        return new TransactionCreatedResponseDto(
                model.getTransactionId(),
                model.getStatus(),
                model.getCreatedDate()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<Transaction> findAllByOwnerId(Long ownerId, PageDataDomain pageDataDomain) {
        Pageable pageable = PageRequest.of(
                pageDataDomain.getPage(),
                pageDataDomain.getSize(),
                pageSortMapper.domainToSort(pageDataDomain.getSort()));
        return pageMapper.converter( transactionRepository.findAllByUserId(ownerId, pageable)
                .map(c -> new TransactionMapper().transactionEntityToModel(c)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Transaction> getById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .map(c -> new TransactionMapper().transactionEntityToModel(c));
    }

    @Override
    public void deleteTransaction(UUID transactionId) {
        transactionRepository.deleteById(transactionId);
    }

}
