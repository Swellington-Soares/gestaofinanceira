package dev.suel.mstransactionapi.infra.services;

import dev.suel.mstransactionapi.application.gateway.TransactionReportPort;
import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
import dev.suel.mstransactionapi.infra.mapper.PageMapper;
import dev.suel.mstransactionapi.infra.mapper.PageSortMapper;
import dev.suel.mstransactionapi.infra.persistence.TransactionEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionReportService implements TransactionReportPort {

    private final TransactionEntityRepository repository;
    private final PageSortMapper pageSortMapper;
    private final PageMapper pageMapper;

    @Override
    public PaginatedResponse<ExpenseByCategory> totalSummaryByCategoryPaginated(Long userId,
                                                                                LocalDateTime startDate,
                                                                                LocalDateTime endDate,
                                                                                PageDataDomain pageData) {
        Pageable pageable = PageRequest.of(
                pageData.getPage(),
                pageData.getSize(),
                pageSortMapper.domainToSort(pageData.getSort()));

        return pageMapper.converter(
                repository.getSummaryOfTransactionOfUserByCategoryPaginated( userId, startDate, endDate, pageable ));
    }

    @Override
    public PaginatedResponse<ExpenseByDay> totalSummaryByDayPaginated(Long userId,
                                                                           LocalDateTime startDate,
                                                                           LocalDateTime endDate,
                                                                           PageDataDomain pageData) {

        Pageable pageable = PageRequest.of(
                pageData.getPage(),
                pageData.getSize(),
                pageSortMapper.domainToSort(pageData.getSort()));

        return  pageMapper.converter(
                repository.getSummaryOfTransactionOfUserByDayPaginated( userId, startDate, endDate, pageable ));
    }

    @Override
    public PaginatedResponse<ExpenseByMonth> totalSummaryByMonthPaginated(Long userId,
                                                                             LocalDateTime startDate,
                                                                             LocalDateTime endDate,
                                                                             PageDataDomain pageData) {

        Pageable pageable = PageRequest.of(
                pageData.getPage(),
                pageData.getSize(),
                pageSortMapper.domainToSort(pageData.getSort()));

        return pageMapper.converter(
                repository.getSummaryOfTransactionOfUserByMonthPaginated( userId, startDate, endDate, pageable ));
    }

    @Override
    public List<ExpenseByDay> totalSummaryByDayAsList(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.totalByDay(userId, startDate, endDate);
    }

    @Override
    public List<ExpenseByMonth> totalSummaryByMonthAsList(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.totalByMonth(userId, startDate, endDate);
    }

    @Override
    public List<ExpenseByCategory> totalSummaryByCategoryAsList(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.totalByCategory(userId, startDate, endDate);
    }


}
