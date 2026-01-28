package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionStatus;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;
import dev.suel.mstransactionapi.infra.web.dto.TransactionTransferCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

class TestCreateTransferTransactionUseCase {

    private TransactionServicePort transactionServicePort;
    private CreateTransferTransactionUseCase useCase;

    @BeforeEach
    void setup() {
        transactionServicePort = mock(TransactionServicePort.class);
        useCase = new CreateTransferTransactionUseCase(transactionServicePort);
    }

    @Test
    void shouldCreateTransferTransactionAndReturnServiceResponse() {
        Long ownerId = 10L;
        TransactionTransferCreateRequest request =
                new TransactionTransferCreateRequest(new BigDecimal("250.00"), 99L);

        TransactionCreatedResponseDto expectedResponse =
                new TransactionCreatedResponseDto(UUID.randomUUID(), TransactionStatus.PENDING, LocalDateTime.now());

        given(transactionServicePort.save(any(Transaction.class))).willReturn(expectedResponse);

        TransactionCreatedResponseDto result = useCase.execute(ownerId, request);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        then(transactionServicePort).should().save(captor.capture());

        Transaction saved = captor.getValue();

        assertThat(saved.getUserId()).isEqualTo(ownerId);
        assertThat(saved.getAmount()).isEqualTo(request.amount());
        assertThat(saved.getOperationType()).isEqualTo(OperationType.TRANSFER);
        assertThat(saved.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(saved.getCurrencyType()).isEqualTo(CurrencyType.Default());
        assertThat(saved.getDestAccountId()).isEqualTo(request.destAccount());
        assertThat(saved.getCreatedDate()).isNotNull();

        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    void shouldThrowExceptionWhenOwnerIdIsNull() {
        TransactionTransferCreateRequest request =
                new TransactionTransferCreateRequest(new BigDecimal("100.00"), 99L);

        assertThatThrownBy(() -> useCase.execute(null, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id do solicitante não foi informado.");
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        assertThatThrownBy(() -> useCase.execute(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O valor da transação é inválido.");
    }

    @Test
    void shouldThrowExceptionWhenAmountIsInvalid() {
        TransactionTransferCreateRequest request =
                new TransactionTransferCreateRequest(BigDecimal.ZERO, 99L);

        assertThatThrownBy(() -> useCase.execute(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O valor da transação é inválido.");
    }
}
