package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.gateway.TransactionReportPort;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
import dev.suel.mstransactionapi.dto.ExpenseReportData;

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

    public List<ExpenseByCategory> totalByCategory(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate.isAfter(LocalDate.now()) || startDate.isAfter(endDate))
            throw new IllegalArgumentException("Data de início inválida.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("Data de fim inválida.");

        return transactionReportPort.totalByCategory(userId, LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX));
    }

    public List<ExpenseByDay> totalByDay(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate.isAfter(LocalDate.now()) || startDate.isAfter(endDate))
            throw new IllegalArgumentException("Data de início inválida.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("Data de fim inválida.");

        return transactionReportPort.totalByDay(userId, LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX));
    }

    public List<ExpenseByMonth> totalByMonth(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate.isAfter(LocalDate.now()) || startDate.isAfter(endDate))
            throw new IllegalArgumentException("Data de início inválida.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("Data de fim inválida.");

        return transactionReportPort.totalByMonth(userId,
                LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX)
        );

    }




}
