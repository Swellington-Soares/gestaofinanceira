package dev.suel.mstransactionapi.infra.web.controller;

import dev.suel.mstransactionapi.application.usecase.GeneratePDFDocumentUseCase;
import dev.suel.mstransactionapi.application.usecase.TransactionReportUseCase;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseAnalysisController {

    private final TransactionReportUseCase transactionReportUseCase;
    private final GeneratePDFDocumentUseCase generatePDFDocumentUseCase;


    @GetMapping("/{userId}/summary/category")
    public ResponseEntity<List<ExpenseByCategory>> totalByCategory(
            @PathVariable Long userId,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return ResponseEntity.ok(
                transactionReportUseCase.totalByCategory(userId, startDate, endDate)
        );
    }


    @GetMapping("/{userId}/summary/day")
    public ResponseEntity<List<ExpenseByDay>> totalByDay(
            @PathVariable Long userId,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return ResponseEntity.ok(
                transactionReportUseCase.totalByDay(userId, startDate, endDate)
        );
    }


    @GetMapping("/{userId}/summary/month")
    public ResponseEntity<List<ExpenseByMonth>> totalByMonth(
            @PathVariable Long userId,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return ResponseEntity.ok(
                transactionReportUseCase.totalByMonth(userId, startDate, endDate)
        );
    }
    @GetMapping("/{userId}/summary/download")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable Long userId,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        byte[] pdf = generatePDFDocumentUseCase.execute(userId, startDate, endDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=relatorio-transacoes.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }
}
