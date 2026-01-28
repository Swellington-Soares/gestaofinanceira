package dev.suel.mstransactionapi.infra.services;

import org.openpdf.text.pdf.PdfReader;
import org.openpdf.text.pdf.parser.PdfTextExtractor;
import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
import dev.suel.mstransactionapi.dto.ExpenseReportData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TestPDFDocumentGeneratorService {

    private PDFDocumentGeneratorService service;

    @BeforeEach
    void setup() {
        service = new PDFDocumentGeneratorService();
    }

    @Test
    void shouldGeneratePdfWithEmptyMessageWhenReportIsEmpty() throws IOException {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 10);

        ExpenseReportData report = new ExpenseReportData(
                start,
                end,
                List.of(),
                List.of(),
                List.of()
        );

        byte[] bytes = service.generateDocument(report);

        assertThat(bytes).isNotNull();
        assertThat(bytes.length).isGreaterThan(0);

        String text = extractText(bytes);

        assertThat(text).contains("Relatório de Despesas");
        assertThat(text).contains("Período: 01/01/2026 até 10/01/2026");
        assertThat(text).contains("Nenhum lançamento encontrado para o período informado");
    }

    @Test
    void shouldGeneratePdfWithSectionsWhenReportHasData() throws IOException {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 10);

        List<ExpenseByMonth> byMonth = List.of(
                new ExpenseByMonth(2026, 1, new BigDecimal("100.50"))
        );

        List<ExpenseByDay> byDay = List.of(
                new ExpenseByDay(LocalDateTime.of(2026, 1, 2, 10, 0), new BigDecimal("25.00"))
        );

        List<ExpenseByCategory> byCategory = List.of(
                new ExpenseByCategory(OperationType.DEPOSIT, new BigDecimal("75.50"))
        );

        ExpenseReportData report = new ExpenseReportData(start, end, byMonth, byDay, byCategory);

        byte[] bytes = service.generateDocument(report);

        assertThat(bytes).isNotNull();
        assertThat(bytes.length).isGreaterThan(0);

        String text = extractText(bytes);

        assertThat(text).contains("Relatório de Despesas");
        assertThat(text).contains("Período: 01/01/2026 até 10/01/2026");

        assertThat(text).contains("Resumo por Mês");
        assertThat(text).contains("Ano");
        assertThat(text).contains("Mês");
        assertThat(text).contains("Total");
        assertThat(text).contains("2026");
        assertThat(text).contains("1");
        assertThat(text).contains("R$ 100.50");

        assertThat(text).contains("Resumo por Dia");
        assertThat(text).contains("Data");
        assertThat(text).contains("R$ 25.00");

        assertThat(text).contains("Resumo por Categoria");
        assertThat(text).contains("Categoria");
        assertThat(text).contains("DEPÓSITO");
        assertThat(text).contains("R$ 75.50");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenReportHasNullLists() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 10);

        ExpenseReportData report = new ExpenseReportData(start, end, null, null, null);

        assertThatThrownBy(() -> service.generateDocument(report))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Erro ao gerar relatório de despesas");
    }

    private String extractText(byte[] pdfBytes) throws IOException {
        PdfReader reader = new PdfReader(pdfBytes);
        PdfTextExtractor extractor = new PdfTextExtractor(reader);

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            sb.append(extractor.getTextFromPage(i)).append("\n");
        }

        reader.close();
        return sb.toString();
    }

}
