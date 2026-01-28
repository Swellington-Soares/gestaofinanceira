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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
    void shouldThrowWhenTransactionNotFound() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        UUID transactionId = UUID.randomUUID();
        String message = "any error";

        given(event.transactionId()).willReturn(transactionId);
        given(transactionRepository.findById(transactionId)).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(event, message));

        then(transactionRepository).should().findById(transactionId);
        verifyNoMoreInteractions(transactionRepository, transactionMapper);
    }


    @Test
    void shouldDoNothingWhenTransactionIsNotPending() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        UUID transactionId = UUID.randomUUID();
        String message = "any error";

        TransactionEntity entity = mock(TransactionEntity.class);
        Transaction transaction = mock(Transaction.class);

        given(event.transactionId()).willReturn(transactionId);
        given(transactionRepository.findById(transactionId)).willReturn(Optional.of(entity));
        given(transactionMapper.transactionEntityToModel(entity)).willReturn(transaction);
        given(transaction.isPending()).willReturn(false);

        useCase.execute(event, message);

        then(transactionRepository).should().findById(transactionId);
        then(transactionMapper).should().transactionEntityToModel(entity);
        then(transaction).should().isPending();
        verifyNoMoreInteractions(transactionRepository, transactionMapper);
    }

    @Test
    void shouldRejectAndSaveWhenTransactionIsPending() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        UUID transactionId = UUID.randomUUID();
        String message = "any error";

        TransactionEntity entity = mock(TransactionEntity.class);
        Transaction transaction = mock(Transaction.class);
        TransactionEntity entityToSave = mock(TransactionEntity.class);

        given(event.transactionId()).willReturn(transactionId);
        given(transactionRepository.findById(transactionId)).willReturn(Optional.of(entity));
        given(transactionMapper.transactionEntityToModel(entity)).willReturn(transaction);
        given(transaction.isPending()).willReturn(true);
        given(transactionMapper.modelToEntity(transaction)).willReturn(entityToSave);

        useCase.execute(event, message);

        then(transactionRepository).should().findById(transactionId);
        then(transactionMapper).should().transactionEntityToModel(entity);
        then(transaction).should().isPending();
        then(transaction).should().reject("Falha técnica após múltiplas tentativas: " + message);
        then(transactionMapper).should().modelToEntity(transaction);
        then(transactionRepository).should().save(entityToSave);

        verifyNoMoreInteractions(transactionRepository, transactionMapper);
    }
}
