package dev.suel.mstransactionprocessor.infra.service;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionprocessor.domain.entity.Transaction;
import dev.suel.mstransactionprocessor.infra.mapper.TransactionMapper;
import dev.suel.mstransactionprocessor.infra.persistence.TransactionEntity;
import dev.suel.mstransactionprocessor.infra.persistence.TransactionEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class TestProcessTransactionRejectUseCaseImpl {

    private TransactionEntityRepository transactionRepository;
    private TransactionMapper transactionMapper;

    private ProcessTransactionRejectUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionEntityRepository.class);
        transactionMapper = mock(TransactionMapper.class);
        useCase = new ProcessTransactionRejectUseCaseImpl(transactionRepository, transactionMapper);
    }

    @Test
    void shouldDoNothingWhenTransactionNotFound() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        UUID transactionId = UUID.randomUUID();
        String message = "any error";

        given(event.id()).willReturn(transactionId);
        given(transactionRepository.findByIdAndIsPending(transactionId)).willReturn(Optional.empty());

        useCase.execute(event, message);

        then(transactionRepository).should().findByIdAndIsPending(transactionId);
        then(transactionRepository).should(never()).save(any());
        then(transactionMapper).shouldHaveNoInteractions();
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void shouldRejectAndSaveWhenTransactionIsPendingAndFoundByRepository() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        UUID transactionId = UUID.randomUUID();
        String message = "any error";

        TransactionEntity entity = mock(TransactionEntity.class);
        Transaction transaction = mock(Transaction.class);
        TransactionEntity entityToSave = mock(TransactionEntity.class);

        given(event.id()).willReturn(transactionId);
        given(transactionRepository.findByIdAndIsPending(transactionId)).willReturn(Optional.of(entity));
        given(transactionMapper.transactionEntityToModel(entity)).willReturn(transaction);
        given(transactionMapper.modelToEntity(transaction)).willReturn(entityToSave);

        useCase.execute(event, message);

        then(transactionRepository).should().findByIdAndIsPending(transactionId);
        then(transactionMapper).should().transactionEntityToModel(entity);
        then(transaction).should().reject("Falha técnica após múltiplas tentativas: " + message);
        then(transactionMapper).should().modelToEntity(transaction);
        then(transactionRepository).should().save(entityToSave);

        verifyNoMoreInteractions(transactionRepository, transactionMapper);
    }

    @Test
    void shouldDoNothingWhenMapperThrowsException() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        UUID transactionId = UUID.randomUUID();
        String message = "any error";

        TransactionEntity entity = mock(TransactionEntity.class);

        given(event.id()).willReturn(transactionId);
        given(transactionRepository.findByIdAndIsPending(transactionId)).willReturn(Optional.of(entity));
        given(transactionMapper.transactionEntityToModel(entity)).willThrow(new RuntimeException("boom"));

        useCase.execute(event, message);

        then(transactionRepository).should().findByIdAndIsPending(transactionId);
        then(transactionRepository).should(never()).save(any());
        then(transactionMapper).should().transactionEntityToModel(entity);
        then(transactionMapper).should(never()).modelToEntity(any());
        verifyNoMoreInteractions(transactionRepository, transactionMapper);
    }
}
