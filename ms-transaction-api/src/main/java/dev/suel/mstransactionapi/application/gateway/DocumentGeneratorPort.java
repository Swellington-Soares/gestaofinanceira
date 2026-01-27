package dev.suel.mstransactionapi.application.gateway;

import dev.suel.mstransactionapi.domain.DocumentInfo;
import dev.suel.mstransactionapi.dto.ExpenseReportData;

import java.util.Map;

public interface DocumentGeneratorPort {
   byte[] generateDocument(ExpenseReportData expenseReportData);
}
