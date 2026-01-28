package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.exception.AccessDeniedException;
import dev.suel.mstransactionapi.application.exception.InvalidOperationException;
import dev.suel.mstransactionapi.application.exception.ResourceNotFoundException;
import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.gestaofinanceira.types.TransactionStatus;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

class TestDeleteTransactionUseCase {

    private TransactionServicePort transactionServicePort;
    private DeleteTransactionUseCase useCase;

    @BeforeEach
    void setup() {
        transactionServicePort = mock(TransactionServicePort.class);
        useCase = new DeleteTransactionUseCase(transactionServicePort);
    }

    @Test
    void shouldThrowResourceNotFoundWhenTransactionDoesNotExist() {
        UUID transactionId = UUID.randomUUID();
        Long ownerId = 10L;

        given(transactionServicePort.getById(transactionId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(transactionId, ownerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("A transação com ID: " + transactionId + " não existe.");

        then(transactionServicePort).should(never()).deleteTransaction(any(UUID.class));
    }

    @Test
    void shouldThrowAccessDeniedWhenOwnerIsNotTheSame() {
        UUID transactionId = UUID.randomUUID();
        Long ownerId = 10L;

        Transaction transaction = mock(Transaction.class);

        given(transactionServicePort.getById(transactionId)).willReturn(Optional.of(transaction));
        given(transaction.isOwner(ownerId)).willReturn(false);

        assertThatThrownBy(() -> useCase.execute(transactionId, ownerId))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Você não pode realizar esta ação.");

        then(transactionServicePort).should(never()).deleteTransaction(any(UUID.class));
    }

    @Test
    void shouldThrowInvalidOperationWhenTransactionIsPending() {
        UUID transactionId = UUID.randomUUID();
        Long ownerId = 10L;

        Transaction transaction = mock(Transaction.class);

        given(transactionServicePort.getById(transactionId)).willReturn(Optional.of(transaction));
        given(transaction.isOwner(ownerId)).willReturn(true);
        given(transaction.getStatus()).willReturn(TransactionStatus.PENDING);

        assertThatThrownBy(() -> useCase.execute(transactionId, ownerId))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("Esta operação não pode ser executada no momento. Transação indisponível.");

        then(transactionServicePort).should(never()).deleteTransaction(any(UUID.class));
    }

    @Test
    void shouldDeleteTransactionWhenAllValidationsPass() {
        UUID transactionId = UUID.randomUUID();
        Long ownerId = 10L;

        Transaction transaction = mock(Transaction.class);

        given(transactionServicePort.getById(transactionId)).willReturn(Optional.of(transaction));
        given(transaction.isOwner(ownerId)).willReturn(true);
        given(transaction.getStatus()).willReturn(TransactionStatus.APPROVED);

        assertThatCode(() -> useCase.execute(transactionId, ownerId))
                .doesNotThrowAnyException();

        then(transactionServicePort).should().deleteTransaction(transactionId);
    }
}
