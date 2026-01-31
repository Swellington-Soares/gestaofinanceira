package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.gateway.DocumentGeneratorPort;
import dev.suel.mstransactionapi.application.gateway.TransactionReportPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class TestGeneratePDFDocumentUseCase {

    private TransactionReportPort transactionReportPort;
    private DocumentGeneratorPort documentGeneratorPort;
    private GeneratePDFDocumentUseCase useCase;

    @BeforeEach
    void setup() {
        transactionReportPort = mock(TransactionReportPort.class);
        documentGeneratorPort = mock(DocumentGeneratorPort.class);
        useCase = new GeneratePDFDocumentUseCase(transactionReportPort, documentGeneratorPort);
    }


    @Test
    void shouldThrowExceptionWhenStartDateIsAfterToday() {
        Long userId = 10L;
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(2);

        assertThatThrownBy(() -> useCase.execute(userId, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de início inválida.");

        then(transactionReportPort).shouldHaveNoInteractions();
        then(documentGeneratorPort).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
        Long userId = 10L;
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().minusDays(10);

        assertThatThrownBy(() -> useCase.execute(userId, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de início inválida.");

        then(transactionReportPort).shouldHaveNoInteractions();
        then(documentGeneratorPort).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        Long userId = 10L;
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().minusDays(2);

        assertThatThrownBy(() -> useCase.execute(userId, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de início inválida.");

        then(transactionReportPort).shouldHaveNoInteractions();
        then(documentGeneratorPort).shouldHaveNoInteractions();
    }
}
