package dev.suel.mstransactionapi.infra.services;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.gestaofinanceira.types.TransactionStatus;
import dev.suel.mstransactionapi.application.gateway.TransactionEventPublisher;
import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;
import dev.suel.mstransactionapi.infra.mapper.PageMapper;
import dev.suel.mstransactionapi.infra.mapper.PageSortMapper;
import dev.suel.mstransactionapi.infra.mapper.TransactionMapper;
import dev.suel.mstransactionapi.infra.persistence.TransactionEntity;
import dev.suel.mstransactionapi.infra.persistence.TransactionEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

class TestTransactionService {

    private TransactionEntityRepository transactionRepository;
    private TransactionMapper mapper;
    private TransactionEventPublisher eventPublisher;

    private PageSortMapper pageSortMapper;
    private PageMapper pageMapper;

    private TransactionService service;

    @BeforeEach
    void setup() {
        transactionRepository = mock(TransactionEntityRepository.class);
        mapper = mock(TransactionMapper.class);
        eventPublisher = mock(TransactionEventPublisher.class);

        pageSortMapper = new PageSortMapper();
        pageMapper = new PageMapper();

        service = new TransactionService(
                transactionRepository,
                mapper,
                eventPublisher,
                pageSortMapper,
                pageMapper
        );
    }

    @AfterEach
    void tearDown() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void shouldSaveEntityRegisterAfterCommitAndPublishEvent() throws Exception {
        TransactionSynchronizationManager.initSynchronization();

        UUID transactionId = UUID.randomUUID();
        TransactionStatus status = TransactionStatus.PENDING;
        LocalDateTime createdDate = LocalDateTime.now();

        Transaction model = mock(Transaction.class);
        given(model.getId()).willReturn(transactionId);
        given(model.getStatus()).willReturn(status);
        given(model.getCreatedDate()).willReturn(createdDate);

        TransactionEntity entity = mock(TransactionEntity.class);
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);

        given(mapper.modelToEntity(model)).willReturn(entity);
        given(transactionRepository.save(entity)).willReturn(entity);
        given(mapper.modelToKafkaData(model)).willReturn(event);

        CountDownLatch latch = new CountDownLatch(1);
        willAnswer(invocation -> {
            latch.countDown();
            return null;
        }).given(eventPublisher).publish(event);

        TransactionCreatedResponseDto result = service.save(model);

        then(mapper).should().modelToEntity(model);
        then(transactionRepository).should().save(entity);
        then(mapper).should().modelToKafkaData(model);

        List<TransactionSynchronization> syncs = TransactionSynchronizationManager.getSynchronizations();
        assertThat(syncs).hasSize(1);

        syncs.get(0).afterCommit();

        assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
        then(eventPublisher).should().publish(event);

        TransactionCreatedResponseDto expected =
                new TransactionCreatedResponseDto(transactionId, status, createdDate);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldFindAllByOwnerIdBuildPageableAndReturnPaginatedResponse() {
        Long ownerId = 10L;

        PageDataDomain pageDataDomain = new PageDataDomain(
                1,
                20,
                List.of(
                        new PageDataDomain.SortField("createdDate", "DESC"),
                        new PageDataDomain.SortField("amount", "ASC")
                )
        );

        given(transactionRepository.findAllByUserId(eq(ownerId), any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of()));

        PaginatedResponse<Transaction> result = service.findAllByOwnerId(ownerId, pageDataDomain);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        then(transactionRepository).should().findAllByUserId(eq(ownerId), pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(20);

        List<org.springframework.data.domain.Sort.Order> orders = pageable.getSort().toList();
        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getProperty()).isEqualTo("createdDate");
        assertThat(orders.get(0).getDirection().name()).isEqualTo("DESC");
        assertThat(orders.get(1).getProperty()).isEqualTo("amount");
        assertThat(orders.get(1).getDirection().name()).isEqualTo("ASC");

        assertThat(result).isNotNull();
        assertThat(result.getData()).isEmpty();
        assertThat(result.getCurrentPage()).isEqualTo(0);
        assertThat(result.getPageSize()).isEqualTo(0);
    }

    @Test
    void shouldReturnEmptyOptionalWhenGetByIdNotFound() {
        UUID transactionId = UUID.randomUUID();

        given(transactionRepository.findById(transactionId)).willReturn(Optional.empty());

        Optional<Transaction> result = service.getById(transactionId);

        then(transactionRepository).should().findById(transactionId);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldDeleteTransaction() {
        UUID transactionId = UUID.randomUUID();

        service.deleteTransaction(transactionId);

        then(transactionRepository).should().deleteById(transactionId);
    }
}
