package dev.suel.msuser.application.gateway;

/**
 * Porta responsável pelo processo de autenticação de usuários
 * no sistema.
 * <p>
 * Esta interface pertence à camada de aplicação (Application Layer)
 * e define um contrato para autenticação, desacoplando os casos
 * de uso dos detalhes de infraestrutura, mecanismos de segurança
 * ou provedores de identidade.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Validar as credenciais informadas pelo usuário</li>
 *     <li>Iniciar o fluxo de autenticação</li>
 *     <li>Delegar a validação para o mecanismo apropriado</li>
 * </ul>
 *
 * <h2>Contexto de Uso</h2>
 * <p>
 * Este port é utilizado em fluxos de login, onde o usuário
 * fornece email e senha para autenticação.
 * </p>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Esta interface não deve retornar tokens ou dados sensíveis</li>
 *     <li>Falhas de autenticação devem ser sinalizadas por exceções</li>
 *     <li>Regras de segurança e hashing são responsabilidade da implementação</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.msuser.infra.services.AuthenticationService} (exemplo – editar caminho)</li>
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface AuthenticationServicePort {

    /**
     * Realiza a autenticação do usuário a partir das credenciais informadas.
     *
     * @param email email do usuário
     * @param password senha do usuário em texto puro
     *
     * @throws RuntimeException caso as credenciais sejam inválidas
     *                          ou ocorra erro durante a autenticação.
     */
    void authenticate(String email, String password);
}
