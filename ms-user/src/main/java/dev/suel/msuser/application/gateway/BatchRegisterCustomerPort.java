package dev.suel.msuser.application.gateway;

import dev.suel.msuser.domain.FileUploadStatus;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Porta responsável pelo processamento de cadastro em lote de clientes
 * a partir de arquivos enviados para o sistema.
 * <p>
 * Esta interface pertence à camada de aplicação (Application Layer)
 * e define um contrato para verificação de status, controle de
 * idempotência e processamento de arquivos de cadastro em lote,
 * desacoplando os casos de uso dos detalhes de leitura de arquivos,
 * persistência e infraestrutura.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Consultar o status de processamento de um upload</li>
 *     <li>Gerar um identificador (hash) para controle de duplicidade</li>
 *     <li>Processar o arquivo de cadastro em lote</li>
 * </ul>
 *
 * <h2>Fluxo Geral</h2>
 * <ol>
 *     <li>Receber o arquivo de cadastro em lote</li>
 *     <li>Gerar um hash para identificar o upload</li>
 *     <li>Verificar se o arquivo já foi processado</li>
 *     <li>Processar o conteúdo e atualizar o status</li>
 * </ol>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>O processamento deve ser idempotente</li>
 *     <li>O {@link BufferedInputStream} deve ser tratado com cuidado para evitar vazamentos de recursos</li>
 *     <li>Leitura e fechamento do stream devem ser responsabilidade da implementação</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.msuser.infra.services.BatchRegisterCustomerService} (exemplo – editar caminho)</li>
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface BatchRegisterCustomerPort {

    /**
     * Consulta o status atual do processamento de um upload de cadastro em lote.
     *
     * @param id identificador do upload ou do arquivo processado
     * @return status atual do upload {@link FileUploadStatus}
     *
     * @throws RuntimeException caso ocorra erro durante a consulta do status
     */
    FileUploadStatus checkStatus(String id);

    /**
     * Gera um código hash a partir do conteúdo do arquivo enviado,
     * utilizado para controle de duplicidade e idempotência.
     *
     * @param stream stream do arquivo de cadastro em lote
     * @return código hash gerado a partir do conteúdo do arquivo
     *
     * @throws IOException caso ocorra erro durante a leitura do stream
     */
    String getHashCode(BufferedInputStream stream) throws IOException;

    /**
     * Processa o arquivo de cadastro em lote de clientes,
     * atualizando o status do upload conforme a execução.
     *
     * @param fileUploadStatus status associado ao upload
     *                         {@link FileUploadStatus}
     * @param stream stream do arquivo contendo os dados de cadastro
     *
     * @throws RuntimeException caso ocorra erro durante o processamento
     */
    void process(FileUploadStatus fileUploadStatus, BufferedInputStream stream);
}
