package dev.suel.mstransactionapi.infra.configuration;


import dev.suel.mstransactionapi.application.gateway.DocumentGeneratorPort;
import dev.suel.mstransactionapi.application.gateway.TransactionReportPort;
import dev.suel.mstransactionapi.application.gateway.TransactionServicePort;
import dev.suel.mstransactionapi.application.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {


    private final TransactionServicePort transactionServicePort;
    private final TransactionReportPort transactionReportPort;
    private final DocumentGeneratorPort pdfDocumentGeneratorService;

    @Bean
    public CreateDepositTransactionUseCase createDepositTransactionUseCase() {
        return new CreateDepositTransactionUseCase(transactionServicePort);
    }

    @Bean
    public CreateTransferTransactionUseCase createTransferTransactionUseCase() {
        return new CreateTransferTransactionUseCase(transactionServicePort);
    }

    @Bean
    public CreateWithDrawTransactionUseCase createWithDrawTransactionUseCase() {
        return new CreateWithDrawTransactionUseCase(transactionServicePort);
    }

    @Bean
    public DeleteTransactionUseCase deleteTransactionUseCase() {
        return new DeleteTransactionUseCase(transactionServicePort);
    }

    @Bean
    public GeneratePDFDocumentUseCase generatePDFDocumentUseCase() {
        return new GeneratePDFDocumentUseCase(transactionReportPort, pdfDocumentGeneratorService);
    }

    @Bean
    public GetAllTransactionUseCase getAllTransactionUseCase() {
        return new GetAllTransactionUseCase(transactionServicePort);
    }

    @Bean
    public ShowTransactionDetailUseCase showTransactionDetailUseCase() {
        return new ShowTransactionDetailUseCase(transactionServicePort);
    }

    @Bean
    public TransactionReportUseCase transactionReportUseCase() {
        return new TransactionReportUseCase(transactionReportPort);
    }

}
