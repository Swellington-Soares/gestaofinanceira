package dev.suel.mstransactionapi.infra.services;

import dev.suel.mstransactionapi.application.gateway.DocumentGeneratorPort;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
import dev.suel.mstransactionapi.dto.ExpenseReportData;
import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PDFDocumentGeneratorService implements DocumentGeneratorPort {

    private static final Font TITLE =
            new Font(Font.HELVETICA, 16, Font.BOLD);

    private static final Font SECTION =
            new Font(Font.HELVETICA, 12, Font.BOLD);

    private static final Font HEADER =
            new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);

    private static final Font BODY =
            new Font(Font.HELVETICA, 11);


    @Override
    public byte[] generateDocument(ExpenseReportData expenseReportData) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 36, 36, 48, 36);
            PdfWriter.getInstance(document, out);

            document.open();

            addTitle(document);
            addByMonth(document, expenseReportData.expenseByMonth());
            addByDay(document, expenseReportData.expenseByDay());
            addByCategory(document, expenseReportData.expenseByCategory());

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de despesas", e);
        }
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("Relatório de Despesas", TITLE);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
    }

    private void addByMonth(Document document, List<ExpenseByMonth> list)
            throws DocumentException {

        document.add(new Paragraph("Resumo por Mês", SECTION));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(8);
        table.setWidths(new float[]{2, 2, 3});

        addHeader(table, "Ano", "Mês", "Total");

        for (ExpenseByMonth item : list) {
            table.addCell(cell(item.year()));
            table.addCell(cell(String.valueOf(item.month())));
            table.addCell(cell(formatCurrency(item.totalAmount())));
        }

        document.add(table);
    }

    private void addByDay(Document document, List<ExpenseByDay> list)
            throws DocumentException {

        document.add(new Paragraph("Resumo por Dia", SECTION));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(8);
        table.setWidths(new float[]{3, 3});

        addHeader(table, "Data", "Total");

        for (ExpenseByDay item : list) {
            table.addCell(cell(formatDate(item.date())));
            table.addCell(cell(formatCurrency(item.totalAmount())));
        }

        document.add(table);
    }


    private void addByCategory(Document document, List<ExpenseByCategory> list)
            throws DocumentException {

        document.add(new Paragraph("Resumo por Categoria", SECTION));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(8);
        table.setWidths(new float[]{3, 3});

        addHeader(table, "Categoria", "Total");

        for (ExpenseByCategory item : list) {
            table.addCell(cell(item.category().name()));
            table.addCell(cell(formatCurrency(item.totalAmount())));
        }

        document.add(table);
    }

    private void addHeader(PdfPTable table, String... titles) {
        for (String title : titles) {
            PdfPCell cell = new PdfPCell(new Phrase(title, HEADER));
            cell.setBackgroundColor(Color.DARK_GRAY);
            cell.setPadding(6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private PdfPCell cell(Object value) {
        PdfPCell cell = new PdfPCell(new Phrase(
                value != null ? value.toString() : "-", BODY
        ));
        cell.setPadding(6);
        return cell;
    }

    private String formatCurrency(BigDecimal value) {
        if (value == null) return "-";
        return "R$ " + value.setScale(2, RoundingMode.HALF_UP);
    }

    private String formatDate(Object date) {
        if (date instanceof LocalDate ld) {
            return ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        if (date instanceof LocalDateTime ld) {
            return ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return date != null ? date.toString() : "-";
    }


}
