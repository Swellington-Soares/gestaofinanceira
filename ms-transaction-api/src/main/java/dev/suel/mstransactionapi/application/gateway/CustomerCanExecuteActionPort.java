package dev.suel.mstransactionapi.application.gateway;

/**
 * Porta de comunicação responsável por verificar se um cliente
 * está autorizado a executar uma determinada ação no sistema.
 * <p>
 * Esta interface representa um contrato da camada de aplicação
 * (Application Layer), seguindo os princípios da Clean Architecture.
 * A implementação concreta desta porta deve delegar a verificação
 * para um serviço externo ou mecanismo apropriado (ex: outro microserviço,
 * API REST, cache ou regra de negócio específica).
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Validar se o cliente atual pode executar a ação solicitada</li>
 *     <li>Abstrair detalhes de comunicação externa</li>
 *     <li>Permitir fácil substituição de implementação</li>
 * </ul>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Esta interface <strong>não deve</strong> conter lógica de infraestrutura</li>
 *     <li>Regras de fallback (ex: erro de comunicação) devem ser bem definidas</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.mstransactionapi.infra.services.CustomerCanExecuteActionImpl}
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface CustomerCanExecuteActionPort {

    /**
     * Verifica se o cliente está autorizado a executar a ação atual.
     *
     * @return {@code true} se o cliente estiver autorizado,
     *         {@code false} caso contrário.
     *
     * @throws RuntimeException caso ocorra erro técnico na verificação
     *                          (ex: falha de comunicação externa).
     */
    boolean isAllowed();
}
