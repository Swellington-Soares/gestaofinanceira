package dev.suel.mstransactionapi.application.gateway;

import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Porta responsável por fornecer consultas de relatórios e consolidações
 * de despesas/transações, permitindo a geração de visões analíticas
 * por categoria, dia e mês.
 * <p>
 * Esta interface pertence à camada de aplicação (Application Layer) e
 * define um contrato para obtenção de dados agregados, desacoplando a
 * lógica de negócio dos detalhes de persistência (JPA, SQL, QueryDSL, etc).
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Fornecer totais consolidados por categoria, dia e mês</li>
 *     <li>Suportar consultas paginadas e também retornos em lista (sem paginação)</li>
 *     <li>Isolar a camada de aplicação de detalhes de consulta e banco de dados</li>
 * </ul>
 *
 * <h2>Regras Gerais</h2>
 * <ul>
 *     <li>As consultas consideram o intervalo {@code startDate} até {@code endDate}</li>
 *     <li>O intervalo deve ser fornecido em {@link LocalDateTime} e interpretado pela implementação</li>
 *     <li>O {@code userId} identifica o dono dos dados analisados</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.mstransactionapi.infra.services.TransactionReportService}
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface TransactionReportPort {

    /**
     * Retorna a consolidação total de despesas por categoria, de forma paginada
     * num intervalo de datas.
     *
     * @param userId identificador do usuário dono das transações
     * @param startDate data/hora inicial do intervalo (inclusiva, conforme implementação)
     * @param endDate data/hora final do intervalo (inclusiva/exclusiva, conforme implementação)
     * @param pageData dados de paginação e ordenação {@link PageDataDomain}
     * @return resposta paginada contendo totais por categoria {@link PaginatedResponse} de {@link ExpenseByCategory}
     *
     * @throws RuntimeException caso ocorra falha técnica ao consultar os dados
     */
    PaginatedResponse<ExpenseByCategory> totalSummaryByCategoryPaginated(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            PageDataDomain pageData
    );

    /**
     * Retorna a consolidação total de despesas por dia, de forma paginada
     * num intervalo de datas.
     *
     * @param userId identificador do usuário dono das transações
     * @param startDate data/hora inicial do intervalo (inclusiva, conforme implementação)
     * @param endDate data/hora final do intervalo (inclusiva/exclusiva, conforme implementação)
     * @param pageData dados de paginação e ordenação {@link PageDataDomain}
     * @return resposta paginada contendo totais por dia {@link PaginatedResponse} de {@link ExpenseByDay}
     *
     * @throws RuntimeException caso ocorra falha técnica ao consultar os dados
     */
    PaginatedResponse<ExpenseByDay> totalSummaryByDayPaginated(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            PageDataDomain pageData
    );

    /**
     * Retorna a consolidação total de despesas por mês, de forma paginada,
     * num intervalo de datas.
     *
     * @param userId identificador do usuário dono das transações
     * @param startDate data/hora inicial do intervalo (inclusiva, conforme implementação)
     * @param endDate data/hora final do intervalo (inclusiva/exclusiva, conforme implementação)
     * @param pageData dados de paginação e ordenação {@link PageDataDomain}
     * @return resposta paginada contendo totais por mês {@link PaginatedResponse} de {@link ExpenseByMonth}
     *
     * @throws RuntimeException caso ocorra falha técnica ao consultar os dados
     */
    PaginatedResponse<ExpenseByMonth> totalSummaryByMonthPaginated(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            PageDataDomain pageData
    );

    /**
     * Retorna a consolidação total de despesas por dia, sem paginação,
     * num intervalo de datas.
     * <p>
     * Recomendado para exportações, geração de documentos ou quando a quantidade
     * de registros esperada é controlada.
     * </p>
     *
     * @param userId identificador do usuário dono das transações
     * @param startDate data/hora inicial do intervalo (inclusiva, conforme implementação)
     * @param endDate data/hora final do intervalo (inclusiva/exclusiva, conforme implementação)
     * @return lista contendo totais por dia {@link List} de {@link ExpenseByDay}
     *
     * @throws RuntimeException caso ocorra falha técnica ao consultar os dados
     */
    List<ExpenseByDay> totalSummaryByDayAsList(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * Retorna a consolidação total de despesas por mês, sem paginação,
     * num intervalo de datas.
     * <p>
     * Recomendado para exportações, geração de documentos ou quando a quantidade
     * de registros esperada é controlada.
     * </p>
     *
     * @param userId identificador do usuário dono das transações
     * @param startDate data/hora inicial do intervalo (inclusiva, conforme implementação)
     * @param endDate data/hora final do intervalo (inclusiva/exclusiva, conforme implementação)
     * @return lista contendo totais por mês {@link List} de {@link ExpenseByMonth}
     *
     * @throws RuntimeException caso ocorra falha técnica ao consultar os dados
     */
    List<ExpenseByMonth> totalSummaryByMonthAsList(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * Retorna a consolidação total de despesas por categoria, sem paginação,
     * num intervalo de datas.
     * <p>
     * Recomendado para exportações, geração de documentos ou quando a quantidade
     * de categorias retornadas é pequena e previsível.
     * </p>
     *
     * @param userId identificador do usuário dono das transações
     * @param startDate data/hora inicial do intervalo (inclusiva, conforme implementação)
     * @param endDate data/hora final do intervalo (inclusiva/exclusiva, conforme implementação)
     * @return lista contendo totais por categoria {@link List} de {@link ExpenseByCategory}
     *
     * @throws RuntimeException caso ocorra falha técnica ao consultar os dados
     */
    List<ExpenseByCategory> totalSummaryByCategoryAsList(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

}
