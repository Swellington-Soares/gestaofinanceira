package dev.suel.mstransactionprocessor.infra.kafka;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionRejectUseCase;
import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class TestTransactionRequestedConsumer {

    private ProcessTransactionUseCase processTransactionUseCase;
    private ProcessTransactionRejectUseCase processTransactionRejectUseCase;

    private TransactionRequestedConsumer consumer;

    @BeforeEach
    void setUp() {
        processTransactionUseCase = mock(ProcessTransactionUseCase.class);
        processTransactionRejectUseCase = mock(ProcessTransactionRejectUseCase.class);
        consumer = new TransactionRequestedConsumer(processTransactionUseCase, processTransactionRejectUseCase);
    }

    @Test
    void shouldConsumeAndExecuteProcessTransactionUseCase() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);

        consumer.consume(event);

        then(processTransactionUseCase).should().execute(event);
        then(processTransactionRejectUseCase).shouldHaveNoInteractions();
        verifyNoMoreInteractions(processTransactionUseCase, processTransactionRejectUseCase);
    }

    @Test
    void shouldConsumeDlqAndExecuteRejectUseCaseWithErrorMessage() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        String errorMessage = "any error";

        consumer.consumeDlq(event, errorMessage);

        then(processTransactionRejectUseCase).should().execute(event, errorMessage);
        then(processTransactionUseCase).shouldHaveNoInteractions();
        verifyNoMoreInteractions(processTransactionUseCase, processTransactionRejectUseCase);
    }

    @Test
    void shouldPropagateExceptionThrownByProcessTransactionUseCase() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        RuntimeException ex = new RuntimeException("boom");
        willThrow(ex).given(processTransactionUseCase).execute(event);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> consumer.consume(event));

        assertSame(ex, thrown);
        then(processTransactionUseCase).should().execute(event);
        then(processTransactionRejectUseCase).shouldHaveNoInteractions();
        verifyNoMoreInteractions(processTransactionUseCase, processTransactionRejectUseCase);
    }
}
