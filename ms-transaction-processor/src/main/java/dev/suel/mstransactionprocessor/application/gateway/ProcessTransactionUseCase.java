package dev.suel.mstransactionprocessor.application.gateway;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;

/**
 * Caso de uso responsável pelo processamento principal de uma transação
 * recebida por meio de eventos.
 * <p>
 * Esta interface representa um contrato da camada de aplicação
 * (Application Layer) e define o fluxo central de processamento
 * de transações financeiras orientadas a eventos, desacoplando
 * o consumo de mensagens das regras de negócio.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Processar transações recebidas via eventos</li>
 *     <li>Aplicar regras de negócio conforme o tipo de operação</li>
 *     <li>Orquestrar chamadas a serviços de saldo, câmbio e validações</li>
 *     <li>Delegar fluxos alternativos (aprovação ou rejeição)</li>
 * </ul>
 *
 * <h2>Contexto de Uso</h2>
 * <p>
 * Este caso de uso é normalmente acionado por um consumidor de mensageria
 * (ex: Kafka Listener) ao receber um {@link TransactionKafkaEventData}.
 * </p>
 *
 * <h2>Fluxo Geral</h2>
 * <ol>
 *     <li>Receber evento de transação</li>
 *     <li>Validar dados essenciais do evento</li>
 *     <li>Aplicar regras de negócio conforme o tipo de transação</li>
 *     <li>Publicar eventos de resultado (aprovação ou rejeição)</li>
 * </ol>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Este caso de uso deve ser idempotente</li>
 *     <li>Falhas devem ser tratadas de forma a permitir retry ou DLT</li>
 *     <li>Não deve conter lógica de infraestrutura ou consumo de mensagens</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.mstransactionprocessor.infra.service.ProcessTransactionUseCaseImpl}
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface ProcessTransactionUseCase {

    /**
     * Executa o processamento de uma transação a partir do evento recebido.
     *
     * @param event dados do evento de transação
     *              {@link TransactionKafkaEventData}
     *
     * @throws RuntimeException caso ocorra erro durante o processamento
     *                          da transação.
     */
    void execute(TransactionKafkaEventData event);
}
