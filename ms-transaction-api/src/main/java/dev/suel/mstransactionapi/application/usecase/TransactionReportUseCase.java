package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.gateway.TransactionReportPort;
import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TransactionReportUseCase {

    private final TransactionReportPort transactionReportPort;


    public TransactionReportUseCase(TransactionReportPort transactionReportPort
                                   ) {
        this.transactionReportPort = transactionReportPort;
    }

    public PaginatedResponse<ExpenseByCategory> totalSummaryByCategoryPaginated(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            PageDataDomain pageDataDomain
    ) {
        if (startDate.isAfter(LocalDate.now()) || startDate.isAfter(endDate))
            throw new IllegalArgumentException("Data de início inválida.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("Data de fim inválida.");

        return transactionReportPort.totalSummaryByCategoryPaginated(
                userId,
                LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX),
                pageDataDomain);
    }

    public PaginatedResponse<ExpenseByDay> totalSummaryByDaysPaginated(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            PageDataDomain pageDataDomain
    ) {
        if (startDate.isAfter(LocalDate.now()) || startDate.isAfter(endDate))
            throw new IllegalArgumentException("Data de início inválida.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("Data de fim inválida.");

        return transactionReportPort.totalSummaryByDayPaginated(
                userId,
                LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX),
                pageDataDomain);
    }

    public PaginatedResponse<ExpenseByMonth> totalSummaryByMonthPaginated(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            PageDataDomain pageDataDomain
    ) {
        if (startDate.isAfter(LocalDate.now()) || startDate.isAfter(endDate))
            throw new IllegalArgumentException("Data de início inválida.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("Data de fim inválida.");

        return transactionReportPort.totalSummaryByMonthPaginated(
                userId,
                LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX),
                pageDataDomain);
    }

    public List<ExpenseByCategory> allTotalSummaryByCategoryAsList(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate.isAfter(LocalDate.now()) || startDate.isAfter(endDate))
            throw new IllegalArgumentException("Data de início inválida.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("Data de fim inválida.");

        return transactionReportPort.totalSummaryByCategoryAsList(userId, LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX));
    }

    public List<ExpenseByDay> allTotalSummaryByDayAsList(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate.isAfter(LocalDate.now()) || startDate.isAfter(endDate))
            throw new IllegalArgumentException("Data de início inválida.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("Data de fim inválida.");

        return transactionReportPort.totalSummaryByDayAsList(userId, LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX));
    }

    public List<ExpenseByMonth> allTotalSummaryByMonthAsList(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate.isAfter(LocalDate.now()) || startDate.isAfter(endDate))
            throw new IllegalArgumentException("Data de início inválida.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("Data de fim inválida.");

        return transactionReportPort.totalSummaryByMonthAsList(userId,
                LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX)
        );

    }




}
