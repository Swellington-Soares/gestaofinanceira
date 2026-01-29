package dev.suel.mstransactionapi.application.gateway;

import dev.suel.mstransactionapi.domain.ExpenseReportData;

public interface DocumentGeneratorPort {
   byte[] generateDocument(ExpenseReportData expenseReportData);
}
