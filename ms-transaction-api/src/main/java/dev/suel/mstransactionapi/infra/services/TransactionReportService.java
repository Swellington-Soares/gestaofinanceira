package dev.suel.mstransactionapi.infra.services;

import dev.suel.mstransactionapi.application.gateway.TransactionReportPort;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
import dev.suel.mstransactionapi.infra.persistence.repository.TransactionEntityRepository;
import dev.suel.mstransactionapi.dto.ExpenseReportData;
import lombok.RequiredArgsConstructor;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.openpdf.text.Document;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionReportService implements TransactionReportPort {

    private final TransactionEntityRepository repository;


    @Override
    public List<ExpenseByCategory> totalByCategory(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.totalByCategory(userId, startDate, endDate);
    }

    @Override
    public List<ExpenseByDay> totalByDay(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.totalByDay(userId, startDate, endDate);
    }

    @Override
    public List<ExpenseByMonth> totalByMonth(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.totalByMonth(userId, startDate, endDate);
    }

    @Override
    public byte[] generateDocument(ExpenseReportData expenseReportData) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();
            document.add(new Paragraph("Olá mundo!"));
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}
