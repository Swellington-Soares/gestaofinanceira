package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.exception.AccessDeniedException;
import dev.suel.mstransactionapi.application.exception.ResourceNotFoundException;
import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionDetailResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

class TestShowTransactionDetailUseCase {

    private TransactionServicePort transactionServicePort;
    private ShowTransactionDetailUseCase useCase;

    @BeforeEach
    void setup() {
        transactionServicePort = mock(TransactionServicePort.class);
        useCase = new ShowTransactionDetailUseCase(transactionServicePort);
    }

    @Test
    void shouldThrowResourceNotFoundWhenTransactionDoesNotExist() {
        UUID transactionId = UUID.randomUUID();
        Long ownerId = 10L;

        given(transactionServicePort.getById(transactionId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(transactionId, ownerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("A transação com ID: " + transactionId + " não existe.");
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
    }

    @Test
    void shouldReturnTransactionDetailResponseWhenOwnerIsValid() {
        UUID transactionId = UUID.randomUUID();
        Long ownerId = 10L;

        Transaction transaction = mock(Transaction.class);

        given(transactionServicePort.getById(transactionId)).willReturn(Optional.of(transaction));
        given(transaction.isOwner(ownerId)).willReturn(true);

        TransactionDetailResponse result = useCase.execute(transactionId, ownerId);

        then(transactionServicePort).should().getById(transactionId);
        assertThat(result).isNotNull();
    }
}
