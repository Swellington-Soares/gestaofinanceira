package dev.suel.mstransactionapi.application.usecase;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionStatus;
import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;
import dev.suel.mstransactionapi.infra.web.dto.TransactionCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

class TestCreateDepositTransactionUseCase {

    private TransactionServicePort transactionServicePort;
    private CreateDepositTransactionUseCase useCase;

    @BeforeEach
    void setup() {
        transactionServicePort = mock(TransactionServicePort.class);
        useCase = new CreateDepositTransactionUseCase(transactionServicePort);
    }

    @Test
    void shouldCreateDepositTransaction() {
        Long ownerId = 10L;
        TransactionCreateRequest request = new TransactionCreateRequest(new BigDecimal("250.00"), CurrencyType.BRL);

        UUID transactionId = UUID.randomUUID();
        TransactionCreatedResponseDto response = new TransactionCreatedResponseDto(transactionId, TransactionStatus.PENDING, LocalDateTime.now());

        given(transactionServicePort.save(any(Transaction.class))).willReturn(response);

        TransactionCreatedResponseDto result = useCase.execute(ownerId, request);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        then(transactionServicePort).should().save(captor.capture());

        Transaction saved = captor.getValue();

        assertThat(saved.getUserId()).isEqualTo(ownerId);
        assertThat(saved.getAmount()).isEqualTo(request.amount());
        assertThat(saved.getOperationType()).isEqualTo(OperationType.DEPOSIT);
        assertThat(saved.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(saved.getCurrencyType()).isEqualTo(CurrencyType.BRL);
        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldThrowExceptionWhenOwnerIdIsNull() {
        TransactionCreateRequest request = new TransactionCreateRequest(new BigDecimal("100.00"), CurrencyType.BRL);

        assertThatThrownBy(() -> useCase.execute(null, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id do solicitante não foi informado.");
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        assertThatThrownBy(() -> useCase.execute(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O valor da transação inválido.");
    }

    @Test
    void shouldThrowExceptionWhenAmountIsInvalid() {
        TransactionCreateRequest request = new TransactionCreateRequest(BigDecimal.ZERO, CurrencyType.BRL);

        assertThatThrownBy(() -> useCase.execute(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O valor da transação inválido.");
    }
}
