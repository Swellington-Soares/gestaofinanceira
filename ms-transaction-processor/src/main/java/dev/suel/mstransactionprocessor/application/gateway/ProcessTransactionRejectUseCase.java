package dev.suel.mstransactionprocessor.application.gateway;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;

/**
 * Caso de uso responsável por processar a rejeição de uma transação,
 * aplicando as regras necessárias e propagando o resultado do processamento.
 * <p>
 * Esta interface representa um contrato da camada de aplicação
 * (Application Layer) e define o fluxo para rejeição de transações
 * recebidas via eventos, normalmente provenientes de um mecanismo
 * de mensageria.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Processar a rejeição de uma transação</li>
 *     <li>Aplicar regras de negócio associadas à rejeição</li>
 *     <li>Atualizar o estado da transação conforme necessário</li>
 *     <li>Propagar o resultado do processamento para outros componentes</li>
 * </ul>
 *
 * <h2>Contexto de Uso</h2>
 * <p>
 * Este caso de uso é geralmente acionado quando uma transação
 * não pode ser concluída com sucesso, como por exemplo:
 * </p>
 * <ul>
 *     <li>Saldo insuficiente</li>
 *     <li>Falha em validações externas</li>
 *     <li>Erro de regra de negócio</li>
 * </ul>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Este caso de uso deve ser idempotente sempre que possível</li>
 *     <li>O evento recebido deve conter dados suficientes para identificar a transação</li>
 *     <li>O motivo da rejeição deve ser registrado e propagado de forma clara</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.mstransactionprocessor.infra.service.ProcessTransactionRejectUseCaseImpl}
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface ProcessTransactionRejectUseCase {

    /**
     * Executa o fluxo de rejeição de uma transação com base no evento recebido.
     *
     * @param event dados do evento de transação
     *              {@link TransactionKafkaEventData}
     * @param message mensagem descritiva indicando o motivo da rejeição
     *
     * @throws RuntimeException caso ocorra erro durante o processamento
     *                          da rejeição da transação.
     */
    void execute(TransactionKafkaEventData event, String message);
}
