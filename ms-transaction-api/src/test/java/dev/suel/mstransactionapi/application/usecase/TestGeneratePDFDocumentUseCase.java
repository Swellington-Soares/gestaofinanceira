package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.gateway.DocumentGeneratorPort;
import dev.suel.mstransactionapi.application.gateway.TransactionReportPort;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
import dev.suel.mstransactionapi.domain.ExpenseReportData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
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
    void shouldGeneratePdfDocumentWithReportData() {
        Long userId = 10L;
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().minusDays(1);

        LocalDateTime expectedS1 = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime expectedS2 = LocalDateTime.of(endDate, LocalTime.MAX);

        List<ExpenseByMonth> byMonth = List.of();
        List<ExpenseByDay> byDay = List.of();
        List<ExpenseByCategory> byCategory = List.of();

        given(transactionReportPort.totalByMonth(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(byMonth);
        given(transactionReportPort.totalByDay(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(byDay);
        given(transactionReportPort.totalByCategory(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(byCategory);

        byte[] pdfBytes = new byte[] {1, 2, 3};
        given(documentGeneratorPort.generateDocument(any(ExpenseReportData.class))).willReturn(pdfBytes);

        byte[] result = useCase.execute(userId, startDate, endDate);

        ArgumentCaptor<LocalDateTime> s1Captor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> s2Captor = ArgumentCaptor.forClass(LocalDateTime.class);

        then(transactionReportPort).should().totalByMonth(eq(userId), s1Captor.capture(), s2Captor.capture());
        assertThat(s1Captor.getValue()).isEqualTo(expectedS1);
        assertThat(s2Captor.getValue()).isEqualTo(expectedS2);

        s1Captor = ArgumentCaptor.forClass(LocalDateTime.class);
        s2Captor = ArgumentCaptor.forClass(LocalDateTime.class);
        then(transactionReportPort).should().totalByDay(eq(userId), s1Captor.capture(), s2Captor.capture());
        assertThat(s1Captor.getValue()).isEqualTo(expectedS1);
        assertThat(s2Captor.getValue()).isEqualTo(expectedS2);

        s1Captor = ArgumentCaptor.forClass(LocalDateTime.class);
        s2Captor = ArgumentCaptor.forClass(LocalDateTime.class);
        then(transactionReportPort).should().totalByCategory(eq(userId), s1Captor.capture(), s2Captor.capture());
        assertThat(s1Captor.getValue()).isEqualTo(expectedS1);
        assertThat(s2Captor.getValue()).isEqualTo(expectedS2);

        ArgumentCaptor<ExpenseReportData> reportCaptor = ArgumentCaptor.forClass(ExpenseReportData.class);
        then(documentGeneratorPort).should().generateDocument(reportCaptor.capture());

        ExpenseReportData report = reportCaptor.getValue();
        assertThat(report.getStartDate()).isEqualTo(startDate);
        assertThat(report.getEndDate()).isEqualTo(endDate);
        assertThat(report.getExpenseByMonth()).isSameAs(byMonth);
        assertThat(report.getExpenseByDay()).isSameAs(byDay);
        assertThat(report.getExpenseByCategory()).isSameAs(byCategory);

        assertThat(result).isSameAs(pdfBytes);
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
