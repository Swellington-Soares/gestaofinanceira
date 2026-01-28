package dev.suel.mstransactionapi.application.usecase;

import dev.suel.mstransactionapi.application.gateway.DocumentGeneratorPort;
import dev.suel.mstransactionapi.application.gateway.TransactionReportPort;
import dev.suel.mstransactionapi.dto.ExpenseReportData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class GeneratePDFDocumentUseCase {

    private final TransactionReportPort transactionReportPort;
    private final DocumentGeneratorPort pdfDocumentGeneratorService;

    public GeneratePDFDocumentUseCase(TransactionReportPort transactionReportPort, DocumentGeneratorPort pdfDocumentGeneratorService) {
        this.transactionReportPort = transactionReportPort;
        this.pdfDocumentGeneratorService = pdfDocumentGeneratorService;
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
                startDate,
                endDate,
                transactionReportPort.totalByMonth(userId, s1, s2),
                transactionReportPort.totalByDay(userId, s1, s2),
                transactionReportPort.totalByCategory(userId, s1, s2)
        );

        return pdfDocumentGeneratorService.generateDocument(expenseReport);
    }
}
