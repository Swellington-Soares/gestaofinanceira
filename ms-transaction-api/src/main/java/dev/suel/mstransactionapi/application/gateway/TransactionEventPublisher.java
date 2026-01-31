package dev.suel.mstransactionapi.application.gateway;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;

/**
 * Porta responsável pela publicação de eventos de transação
 * para sistemas externos por um mecanismo de mensageria.
 * <p>
 * Esta interface pertence à camada de aplicação (Application Layer)
 * e define um contrato para publicação de eventos de domínio,
 * desacoplando completamente o caso de uso dos detalhes de
 * infraestrutura, broker ou tecnologia utilizada.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Publicar eventos de transação para consumo assíncrono</li>
 *     <li>Isolar o uso de mensageria da lógica de negócio</li>
 *     <li>Garantir clareza no contrato de publicação de eventos</li>
 * </ul>
 *
 * <h2>Contexto de Uso</h2>
 * <p>
 * Este publisher é normalmente utilizado após mudanças de estado
 * relevantes de uma transação, como:
 * </p>
 * <ul>
 *     <li>Aprovação</li>
 *     <li>Rejeição</li>
 *     <li>Processamento</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.mstransactionapi.infra.services.KafkaTransactionEventPublisher}
 * </ul>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Esta interface não deve conter dependências de infraestrutura</li>
 *     <li>A publicação deve ser assíncrona sempre que possível</li>
 *     <li>Falhas na publicação devem ser tratadas pela implementação</li>
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface TransactionEventPublisher {

    /**
     * Publica um evento de transação para o mecanismo de mensageria configurado.
     *
     * @param event dados do evento de transação
     *              {@link TransactionKafkaEventData}
     *
     * @throws RuntimeException caso ocorra falha durante a publicação do evento
     *                          (ex: indisponibilidade do broker, erro de serialização).
     */
    void publish(TransactionKafkaEventData event);
}

