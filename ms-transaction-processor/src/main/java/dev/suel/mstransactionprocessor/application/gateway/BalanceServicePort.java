package dev.suel.mstransactionprocessor.application.gateway;

import java.math.BigDecimal;

/**
 * Porta responsável pela interação com o serviço de saldo do usuário,
 * permitindo consulta e atualização de valores monetários.
 * <p>
 * Esta interface pertence à camada de aplicação (Application Layer)
 * e define um contrato para operações relacionadas ao saldo,
 * desacoplando a lógica de negócio dos detalhes de infraestrutura
 * ou comunicação externa.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Consultar o saldo atual de um usuário</li>
 *     <li>Atualizar o saldo do usuário após operações financeiras</li>
 * </ul>
 *
 * <h2>Contexto de Uso</h2>
 * <p>
 * Este port é geralmente utilizado em casos de uso que envolvem
 * processamento de transações financeiras, como:
 * </p>
 * <ul>
 *     <li>Depósitos</li>
 *     <li>Transferências</li>
 *     <li>Saques</li>
 * </ul>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Valores monetários devem utilizar {@link BigDecimal}</li>
 *     <li>A implementação deve garantir consistência e atomicidade</li>
 *     <li>Regras de concorrência devem ser tratadas externamente</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.mstransactionprocessor.infra.service.BalanceServiceImpl}
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface BalanceServicePort {

    /**
     * Atualiza o saldo do usuário com o valor informado.
     * <p>
     * O valor pode representar crédito ou débito, conforme a regra
     * de negócio aplicada pelo caso de uso.
     * </p>
     *
     * @param userId identificador do usuário
     * @param amount valor a ser aplicado ao saldo
     *
     * @throws RuntimeException caso ocorra erro durante a atualização
     */
    void updateBalance(Long userId, BigDecimal amount);

    /**
     * Retorna o saldo atual do usuário.
     *
     * @param userId identificador do usuário
     * @return saldo atual do usuário
     *
     * @throws RuntimeException caso ocorra erro durante a consulta
     */
    BigDecimal getBalance(Long userId);
}
