package dev.suel.mstransactionapi.dto;

import java.time.LocalDate;
import java.util.List;

public class ExpenseReportData {
    private final List<ExpenseByMonth> expenseByMonth;
    private final List<ExpenseByDay> expenseByDay;
    private final List<ExpenseByCategory> expenseByCategory;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public ExpenseReportData(
            LocalDate startDate,
            LocalDate endDate,
            List<ExpenseByMonth> expenseByMonth,
                             List<ExpenseByDay> expenseByDay,
                             List<ExpenseByCategory> expenseByCategory) {
        this.expenseByMonth = expenseByMonth;
        this.expenseByDay = expenseByDay;
        this.expenseByCategory = expenseByCategory;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isEmpty() {
        return expenseByMonth.isEmpty() && expenseByDay.isEmpty() && expenseByCategory.isEmpty();
    }

    public List<ExpenseByMonth> getExpenseByMonth() {
        return expenseByMonth;
    }

    public List<ExpenseByDay> getExpenseByDay() {
        return expenseByDay;
    }

    public List<ExpenseByCategory> getExpenseByCategory() {
        return expenseByCategory;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
