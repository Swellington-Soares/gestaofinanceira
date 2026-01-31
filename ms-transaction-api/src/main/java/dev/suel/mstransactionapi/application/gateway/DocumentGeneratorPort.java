package dev.suel.mstransactionapi.application.gateway;

import dev.suel.mstransactionapi.domain.ExpenseReportData;

/**
 * Porta responsável pela geração de documentos a partir de dados
 * consolidados de relatório de despesas.
 * <p>
 * Esta interface faz parte da camada de aplicação (Application Layer)
 * e define um contrato para geração de documentos (ex: PDF, CSV, Excel),
 * desacoplando completamente a lógica de negócio dos detalhes de
 * formatação, tecnologia ou biblioteca utilizada.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Receber dados prontos para geração do documento</li>
 *     <li>Delegar a conversão desses dados para um formato específico</li>
 *     <li>Abstrair dependências de bibliotecas externas</li>
 * </ul>
 *
 * <h2>Formato do Documento</h2>
 * <p>
 * O formato final do documento é definido pela implementação concreta
 * desta interface, podendo ser:
 * </p>
 * <ul>
 *     <li>PDF</li>
 *     <li>CSV</li>
 *     <li>Excel</li>
 *     <li>Qualquer outro formato necessário</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.mstransactionapi.infra.services.PDFDocumentGeneratorService} </li>
 * </ul>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Esta interface não deve conter lógica de infraestrutura</li>
 *     <li>Os dados fornecidos devem estar previamente validados</li>
 *     <li>Erros técnicos devem ser tratados na implementação</li>
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface DocumentGeneratorPort {

   /**
    * Gera um documento a partir dos dados de relatório de despesas informados.
    *
    * @param expenseReportData dados consolidados do relatório de despesas
    *                          {@link ExpenseReportData}
    * @return array de bytes representando o documento gerado
    *
    * @throws RuntimeException caso ocorra erro durante a geração do documento
    *                          (ex: falha de biblioteca, erro de I/O ou formatação).
    */
   byte[] generateDocument(ExpenseReportData expenseReportData);
}
