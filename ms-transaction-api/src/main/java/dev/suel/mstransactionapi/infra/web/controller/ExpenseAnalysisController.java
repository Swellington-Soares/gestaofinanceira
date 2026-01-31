package dev.suel.mstransactionapi.infra.web.controller;

import dev.suel.mstransactionapi.application.exception.AccessDeniedException;
import dev.suel.mstransactionapi.application.usecase.GeneratePDFDocumentUseCase;
import dev.suel.mstransactionapi.application.usecase.TransactionReportUseCase;
import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
import dev.suel.mstransactionapi.infra.mapper.PageMapper;
import dev.suel.mstransactionapi.infra.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseAnalysisController {

    private final TransactionReportUseCase transactionReportUseCase;
    private final GeneratePDFDocumentUseCase generatePDFDocumentUseCase;
    private final SecurityService securityService;
    private final PageMapper pageMapper;


    @GetMapping("/{userId}/summary/category")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaginatedResponse<ExpenseByCategory>> totalByCategoryPaginated(
            @PathVariable Long userId,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            Pageable pageable,
            Authentication authentication
    ) {
        if (!securityService.isOwner(userId, authentication))
            throw new AccessDeniedException();

        PageDataDomain pageDataDomain = pageMapper.pageableToPageDataDomain(pageable);

        return ResponseEntity.ok(
                transactionReportUseCase.totalSummaryByCategoryPaginated(userId, startDate, endDate, pageDataDomain)
        );
    }


    @GetMapping("/{userId}/summary/day")
    @PreAuthorize("isAuthenticated()")
    public  ResponseEntity<PaginatedResponse<ExpenseByDay>> totalByDayPaginated(
            @PathVariable Long userId,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            Pageable pageable,
            Authentication authentication
    ) {
        if (!securityService.isOwner(userId, authentication))
            throw new AccessDeniedException();

        PageDataDomain pageDataDomain = pageMapper.pageableToPageDataDomain(pageable);

        return ResponseEntity.ok(
                transactionReportUseCase.totalSummaryByDaysPaginated(userId, startDate, endDate,pageDataDomain)
        );
    }


    @GetMapping("/{userId}/summary/month")
    @PreAuthorize("isAuthenticated()")
    public  ResponseEntity<PaginatedResponse<ExpenseByMonth>> totalByMonthPaginated(
            @PathVariable Long userId,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            Pageable pageable,
            Authentication authentication
    ) {
        if (!securityService.isOwner(userId, authentication))
            throw new AccessDeniedException();

        PageDataDomain pageDataDomain = pageMapper.pageableToPageDataDomain(pageable);

        return ResponseEntity.ok(
                transactionReportUseCase.totalSummaryByMonthPaginated(userId, startDate, endDate, pageDataDomain)
        );
    }
    @GetMapping("/{userId}/summary/download")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable Long userId,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            Authentication authentication
    ) {
        if (!securityService.isOwner(userId, authentication))
            throw new AccessDeniedException();

        byte[] pdf = generatePDFDocumentUseCase.execute(userId, startDate, endDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=relatorio-despesas.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }
}
