package dev.suel.mstransactionprocessor.application.gateway;

import dev.suel.gestaofinanceira.types.CurrencyType;

import java.math.BigDecimal;

/**
 * Porta responsável pela obtenção da taxa de câmbio atual
 * para um determinado tipo de moeda.
 * <p>
 * Esta interface pertence à camada de aplicação (Application Layer)
 * e define um contrato para consulta de taxas de câmbio,
 * desacoplando a lógica de negócio dos detalhes de integração
 * com serviços externos ou APIs de câmbio.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Fornecer a taxa de câmbio vigente para a moeda solicitada</li>
 *     <li>Abstrair a origem dos dados de câmbio</li>
 *     <li>Garantir precisão no cálculo financeiro</li>
 * </ul>
 *
 * <h2>Contexto de Uso</h2>
 * <p>
 * Este port é utilizado em casos de uso que envolvem conversão
 * de valores monetários, como processamento de transações
 * internacionais ou consolidação financeira.
 * </p>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Valores monetários devem utilizar {@link BigDecimal}</li>
 *     <li>A taxa retornada deve representar o valor do dia corrente</li>
 *     <li>Cache e políticas de atualização são responsabilidade da implementação</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.mstransactionprocessor.infra.service.ExchangeServiceImpl}
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface ExchangeServicePort {

    /**
     * Retorna a taxa de câmbio do dia para o tipo de moeda informado.
     *
     * @param currencyType tipo da moeda a ser convertida
     *                     {@link CurrencyType}
     * @return taxa de câmbio vigente para a moeda informada
     *
     * @throws RuntimeException caso ocorra erro na obtenção da taxa
     *                          (ex: indisponibilidade do serviço externo).
     */
    BigDecimal getCurrencyExchangeRateToday(CurrencyType currencyType);
}
