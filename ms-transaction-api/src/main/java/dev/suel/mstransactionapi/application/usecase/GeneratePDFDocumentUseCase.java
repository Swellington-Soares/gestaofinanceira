package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.gateway.TransactionReportPort;
import dev.suel.mstransactionapi.dto.ExpenseReportData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class GeneratePDFDocumentUseCase {

    private final TransactionReportPort transactionReportPort;

    public GeneratePDFDocumentUseCase(TransactionReportPort transactionReportPort) {
        this.transactionReportPort = transactionReportPort;
    }

    public byte[] execute(Long userId,
                          LocalDate startDate,
                          LocalDate endDate) {

        if (startDate.isAfter(LocalDate.now()) || startDate.isAfter(endDate))
            throw new IllegalArgumentException("Data de início inválida.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("Data de fim inválida.");

        var s1 = LocalDateTime.of(startDate, LocalTime.MIN);
        var s2 = LocalDateTime.of(endDate, LocalTime.MAX);

        ExpenseReportData expenseReport = new ExpenseReportData(
                transactionReportPort.totalByMonth(userId, s1, s2),
                transactionReportPort.totalByDay(userId, s1, s2),
                transactionReportPort.totalByCategory(userId, s1, s2)
        );

        return transactionReportPort.generateDocument(expenseReport);
    }
}
