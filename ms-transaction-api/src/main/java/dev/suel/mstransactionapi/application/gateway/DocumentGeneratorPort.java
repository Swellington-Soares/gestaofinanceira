package dev.suel.mstransactionapi.application.gateway;

import dev.suel.mstransactionapi.domain.ExpenseReportData;

/**
 * Porta responsável pela geração de documentos a partir de dados de relatório de despesas.
 *
 * <p>
 * Esta interface representa um <b>Port</b> da camada de aplicação, seguindo os princípios
 * da <b>Clean Architecture</b>. Ela define o contrato para geração de documentos,
 * abstraindo a tecnologia ou formato específico (PDF, Excel, CSV, etc.).
 * </p>
 *
 * <p>
 * Implementações concretas deste contrato devem residir na camada de infraestrutura
 * e podem utilizar bibliotecas externas para renderização do documento.
 * </p>
 *
 * <p>
 * O documento gerado é retornado como um array de bytes, permitindo flexibilidade
 * para armazenamento, envio via HTTP ou persistência em sistemas externos.
 * </p>
 *
 * @author SeuNome
 * @since 1.0
 */
public interface DocumentGeneratorPort {

   /**
    * Gera um documento com base nos dados fornecidos do relatório de despesas.
    *
    * @param expenseReportData objeto contendo todas as informações necessárias
    *                          para a composição do documento
    * @return um array de bytes representando o documento gerado
    * @throws IllegalArgumentException caso os dados informados sejam inválidos ou incompletos
    */
   byte[] generateDocument(ExpenseReportData expenseReportData);
}

