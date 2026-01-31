package dev.suel.msuser.application.gateway;

import dev.suel.msuser.domain.PageDataDomain;
import dev.suel.msuser.domain.PaginatedResponse;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.dto.CustomerInfoResponse;

/**
 * Porta responsável pela gestão de operações relacionadas a clientes (customers),
 * incluindo consultas, cadastro, atualização, remoção e alterações de credenciais.
 * <p>
 * Esta interface pertence à camada de aplicação (Application Layer) e define um
 * contrato para operações de cliente, desacoplando os casos de uso dos detalhes
 * de persistência, infraestrutura e frameworks.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Consultar clientes por e-mail e por identificador</li>
 *     <li>Registrar novos clientes e atualizar dados cadastrais</li>
 *     <li>Gerenciar mudança de senha</li>
 *     <li>Remover clientes</li>
 *     <li>Listar clientes de forma paginada</li>
 *     <li>Verificar existência de e-mail</li>
 * </ul>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Validações e regras de negócio devem ser aplicadas antes de persistir alterações</li>
 *     <li>Tratamento de hashing/encoding de senha é responsabilidade da implementação</li>
 *     <li>Falhas funcionais (ex: cliente inexistente) devem ser sinalizadas por exceções apropriadas</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.msuser.infra.services.CustomerService} (exemplo – editar caminho)</li>
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface CustomerServicePort {

    /**
     * Localiza um cliente pelo e-mail.
     *
     * @param email e-mail do cliente
     * @return cliente encontrado {@link Customer}
     *
     * @throws RuntimeException caso o cliente não exista ou ocorra falha na consulta
     */
    Customer findByCustomerEmail(String email);

    /**
     * Registra um novo cliente no sistema.
     *
     * @param customer entidade de domínio com os dados do cliente
     *                 {@link Customer}
     * @return cliente persistido {@link Customer}
     *
     * @throws RuntimeException caso o e-mail já esteja em uso ou ocorra falha na persistência
     */
    Customer registerNewCustomer(Customer customer);

    /**
     * Localiza um cliente pelo seu identificador.
     *
     * @param id identificador do cliente
     * @return cliente encontrado {@link Customer}
     *
     * @throws RuntimeException caso o cliente não exista ou ocorra falha na consulta
     */
    Customer findCustomerById(Long id);

    /**
     * Atualiza os dados cadastrais de um cliente existente.
     *
     * @param customer entidade de domínio com os dados atualizados
     *                 {@link Customer}
     *
     * @throws RuntimeException caso o cliente não exista ou ocorra falha na atualização
     */
    void updateCustomer(Customer customer);

    /**
     * Altera a senha do cliente.
     * <p>
     * A implementação deve validar a política de senha (quando aplicável),
     * aplicar hashing/encoding e persistir a alteração.
     * </p>
     *
     * @param customer cliente a ter a senha alterada {@link Customer}
     * @param novaSenha nova senha em texto puro
     * @return {@code true} se a senha foi alterada com sucesso; {@code false} caso contrário
     *
     * @throws RuntimeException caso ocorra falha durante o processo de alteração
     */
    boolean changeCustomerPassword(Customer customer, String novaSenha);

    /**
     * Remove um cliente pelo seu identificador.
     *
     * @param id identificador do cliente a ser removido
     *
     * @throws RuntimeException caso o cliente não exista ou ocorra falha na remoção
     */
    void removeCustomerById(Long id);

    /**
     * Retorna uma lista paginada de clientes, geralmente utilizada em telas administrativas
     * ou listagens.
     *
     * @param pageData dados de paginação e ordenação {@link PageDataDomain}
     * @return resposta paginada contendo dados resumidos de clientes
     *         {@link PaginatedResponse} de {@link CustomerInfoResponse}
     *
     * @throws RuntimeException caso ocorra falha na consulta
     */
    PaginatedResponse<CustomerInfoResponse> findAll(PageDataDomain pageData);

    /**
     * Verifica se já existe um cliente cadastrado com o e-mail informado.
     *
     * @param email e-mail a ser verificado
     * @return {@code true} se existir; {@code false} caso contrário
     *
     * @throws RuntimeException caso ocorra falha na consulta
     */
    boolean existsByEmail(String email);
}
