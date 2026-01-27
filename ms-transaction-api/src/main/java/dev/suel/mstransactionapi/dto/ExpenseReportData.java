package dev.suel.mstransactionapi.dto;

import java.util.List;

public record ExpenseReportData(
        List<ExpenseByMonth> expenseByMonth,
        List<ExpenseByDay> expenseByDay,
        List<ExpenseByCategory> expenseByCategory
) {
}
