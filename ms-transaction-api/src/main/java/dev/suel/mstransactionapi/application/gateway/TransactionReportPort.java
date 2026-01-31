package dev.suel.mstransactionapi.application.gateway;

import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionReportPort {

    PaginatedResponse<ExpenseByCategory> totalSummaryByCategoryPaginated(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            PageDataDomain pageData
    );

    PaginatedResponse<ExpenseByDay> totalSummaryByDayPaginated(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            PageDataDomain pageData
    );

    PaginatedResponse<ExpenseByMonth> totalSummaryByMonthPaginated(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            PageDataDomain pageData
    );

    List<ExpenseByDay> totalSummaryByDayAsList(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<ExpenseByMonth> totalSummaryByMonthAsList(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<ExpenseByCategory> totalSummaryByCategoryAsList(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

}
