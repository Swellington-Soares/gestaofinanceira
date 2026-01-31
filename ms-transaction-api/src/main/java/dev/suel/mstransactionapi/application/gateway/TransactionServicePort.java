package dev.suel.mstransactionapi.application.gateway;

import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;

import java.util.Optional;
import java.util.UUID;

/**
 * Porta responsável por orquestrar operações relacionadas à gestão
 * de transações dentro da aplicação.
 * <p>
 * Esta interface faz parte da camada de aplicação (Application Layer)
 * e define um contrato para persistência, consulta e remoção de
 * transações, isolando completamente os casos de uso dos detalhes
 * de infraestrutura, persistência ou frameworks utilizados.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Persistir novas transações</li>
 *     <li>Consultar transações por identificador ou proprietário</li>
 *     <li>Fornecer suporte a paginação</li>
 *     <li>Excluir transações existentes</li>
 * </ul>
 *
 * <h2>Observações Arquiteturais</h2>
 * <ul>
 *     <li>Esta porta opera sobre o modelo de domínio {@link Transaction}</li>
 *     <li>Conversões para DTOs devem ser responsabilidade da camada de aplicação</li>
 *     <li>Regras de negócio devem ser aplicadas antes da persistência</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.mstransactionapi.infra.services.TransactionService} (exemplo – editar caminho)</li>
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface TransactionServicePort {

    /**
     * Persiste uma nova transação no sistema.
     *
     * @param model transação de domínio a ser persistida
     *              {@link Transaction}
     * @return DTO contendo informações da transação criada
     *         {@link TransactionCreatedResponseDto}
     *
     * @throws RuntimeException caso ocorra erro durante a persistência
     */
    TransactionCreatedResponseDto save(Transaction model);

    /**
     * Retorna uma lista paginada de transações associadas a um determinado
     * proprietário (usuário).
     *
     * @param ownerId identificador do usuário proprietário das transações
     * @param pageDataDomain dados de paginação e ordenação
     *                       {@link PageDataDomain}
     * @return resposta paginada contendo transações
     *         {@link PaginatedResponse} de {@link Transaction}
     *
     * @throws RuntimeException caso ocorra erro durante a consulta
     */
    PaginatedResponse<Transaction> findAllByOwnerId(
            Long ownerId,
            PageDataDomain pageDataDomain
    );

    /**
     * Recupera uma transação a partir do seu identificador único.
     *
     * @param transactionId identificador único da transação
     * @return {@link Optional} contendo a transação caso encontrada,
     *         ou vazio caso não exista
     *
     * @throws RuntimeException caso ocorra erro durante a consulta
     */
    Optional<Transaction> getById(UUID transactionId);

    /**
     * Remove uma transação do sistema a partir do seu identificador.
     *
     * @param transactionId identificador único da transação a ser removida
     *
     * @throws RuntimeException caso a transação não exista
     *                          ou ocorra erro durante a exclusão
     */
    void deleteTransaction(UUID transactionId);
}
