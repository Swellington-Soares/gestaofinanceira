package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionDetailResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class TestGetAllTransactionUseCase {

    private TransactionServicePort transactionServicePort;
    private GetAllTransactionUseCase useCase;

    @BeforeEach
    void setup() {
        transactionServicePort = mock(TransactionServicePort.class);
        useCase = new GetAllTransactionUseCase(transactionServicePort);
    }

    @Test
    void shouldReturnMappedPaginatedResponse() {
        Long ownerId = 10L;

        PageDataDomain pageDataDomain =
                new PageDataDomain(0, 10, List.of());

        Transaction transaction = mock(Transaction.class);

        PaginatedResponse<Transaction> page =
                new PaginatedResponse<>(
                        List.of(transaction),
                        0, 1, 1, 10,
                        false, false
                );

        given(transactionServicePort.findAllByOwnerId(ownerId, pageDataDomain))
                .willReturn(page);

        PaginatedResponse<TransactionDetailResponse> result =
                useCase.execute(pageDataDomain, ownerId);

        then(transactionServicePort).should()
                .findAllByOwnerId(ownerId, pageDataDomain);

        assertThat(result.getData()).hasSize(1);
        assertThat(result.getCurrentPage()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalItems()).isEqualTo(1);
        assertThat(result.getPageSize()).isEqualTo(10);
    }
}
