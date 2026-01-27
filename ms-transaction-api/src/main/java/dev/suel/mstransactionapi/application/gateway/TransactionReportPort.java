package dev.suel.mstransactionapi.application.gateway;

import dev.suel.mstransactionapi.dto.*;
import dev.suel.mstransactionapi.dto.ExpenseReportData;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionReportPort {


    List<ExpenseByCategory> totalByCategory(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<ExpenseByDay> totalByDay(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<ExpenseByMonth> totalByMonth(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );


}
