package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.gateway.TransactionReportPort;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
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

class TestTransactionReportUseCase {

    private TransactionReportPort transactionReportPort;
    private TransactionReportUseCase useCase;

    @BeforeEach
    void setup() {
        transactionReportPort = mock(TransactionReportPort.class);
        useCase = new TransactionReportUseCase(transactionReportPort);
    }

    @Test
    void shouldReturnTotalByCategory() {
        Long userId = 10L;
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().minusDays(1);

        LocalDateTime expectedS1 = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime expectedS2 = LocalDateTime.of(endDate, LocalTime.MAX);

        List<ExpenseByCategory> expected = List.of();

        given(transactionReportPort.totalByCategory(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(expected);

        List<ExpenseByCategory> result = useCase.totalByCategory(userId, startDate, endDate);

        ArgumentCaptor<LocalDateTime> s1Captor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> s2Captor = ArgumentCaptor.forClass(LocalDateTime.class);

        then(transactionReportPort).should().totalByCategory(eq(userId), s1Captor.capture(), s2Captor.capture());
        assertThat(s1Captor.getValue()).isEqualTo(expectedS1);
        assertThat(s2Captor.getValue()).isEqualTo(expectedS2);

        assertThat(result).isSameAs(expected);
    }

    @Test
    void shouldReturnTotalByDay() {
        Long userId = 10L;
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().minusDays(1);

        LocalDateTime expectedS1 = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime expectedS2 = LocalDateTime.of(endDate, LocalTime.MAX);

        List<ExpenseByDay> expected = List.of();

        given(transactionReportPort.totalByDay(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(expected);

        List<ExpenseByDay> result = useCase.totalByDay(userId, startDate, endDate);

        ArgumentCaptor<LocalDateTime> s1Captor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> s2Captor = ArgumentCaptor.forClass(LocalDateTime.class);

        then(transactionReportPort).should().totalByDay(eq(userId), s1Captor.capture(), s2Captor.capture());
        assertThat(s1Captor.getValue()).isEqualTo(expectedS1);
        assertThat(s2Captor.getValue()).isEqualTo(expectedS2);

        assertThat(result).isSameAs(expected);
    }

    @Test
    void shouldReturnTotalByMonth() {
        Long userId = 10L;
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().minusDays(1);

        LocalDateTime expectedS1 = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime expectedS2 = LocalDateTime.of(endDate, LocalTime.MAX);

        List<ExpenseByMonth> expected = List.of();

        given(transactionReportPort.totalByMonth(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(expected);

        List<ExpenseByMonth> result = useCase.totalByMonth(userId, startDate, endDate);

        ArgumentCaptor<LocalDateTime> s1Captor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> s2Captor = ArgumentCaptor.forClass(LocalDateTime.class);

        then(transactionReportPort).should().totalByMonth(eq(userId), s1Captor.capture(), s2Captor.capture());
        assertThat(s1Captor.getValue()).isEqualTo(expectedS1);
        assertThat(s2Captor.getValue()).isEqualTo(expectedS2);

        assertThat(result).isSameAs(expected);
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsAfterToday() {
        Long userId = 10L;
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(2);

        assertThatThrownBy(() -> useCase.totalByCategory(userId, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de início inválida.");

        assertThatThrownBy(() -> useCase.totalByDay(userId, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de início inválida.");

        assertThatThrownBy(() -> useCase.totalByMonth(userId, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de início inválida.");

        then(transactionReportPort).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
        Long userId = 10L;
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().minusDays(10);

        assertThatThrownBy(() -> useCase.totalByCategory(userId, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de início inválida.");

        assertThatThrownBy(() -> useCase.totalByDay(userId, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de início inválida.");

        assertThatThrownBy(() -> useCase.totalByMonth(userId, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de início inválida.");

        then(transactionReportPort).shouldHaveNoInteractions();
    }
}
