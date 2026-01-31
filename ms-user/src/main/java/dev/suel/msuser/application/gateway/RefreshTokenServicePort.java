package dev.suel.msuser.application.gateway;

import dev.suel.msuser.dto.TokenInfo;

/**
 * Porta responsável pela gestão de refresh tokens no sistema,
 * incluindo geração, validação e atualização.
 * <p>
 * Esta interface pertence à camada de aplicação (Application Layer)
 * e define um contrato para controle do ciclo de vida de refresh tokens,
 * desacoplando os casos de uso dos detalhes de segurança, armazenamento
 * e implementação técnica.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Gerar novos refresh tokens para usuários autenticados</li>
 *     <li>Validar refresh tokens existentes</li>
 *     <li>Atualizar refresh tokens associados a um usuário</li>
 * </ul>
 *
 * <h2>Contexto de Uso</h2>
 * <p>
 * Este port é utilizado em fluxos de autenticação e renovação de sessão,
 * normalmente em conjunto com tokens de acesso (access tokens).
 * </p>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Refresh tokens devem possuir tempo de expiração adequado</li>
 *     <li>A implementação deve prevenir reutilização indevida de tokens</li>
 *     <li>Validações devem considerar expiração, assinatura e integridade</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.msuser.infra.services.RefreshTokenService}
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface RefreshTokenServicePort {

    /**
     * Atualiza o refresh token associado a um usuário.
     *
     * @param email e-mail do usuário
     * @param refreshToken informações do refresh token
     *                     {@link TokenInfo}
     *
     * @throws RuntimeException caso ocorra erro durante a atualização
     */
    void update(String email, TokenInfo refreshToken);

    /**
     * Gera um novo refresh token para o usuário informado.
     *
     * @param email e-mail do usuário
     * @return informações do refresh token gerado {@link TokenInfo}
     *
     * @throws RuntimeException caso ocorra erro durante a geração
     */
    TokenInfo generateToken(String email);

    /**
     * Valida um refresh token e retorna o identificador (subject)
     * associado ao token.
     *
     * @param token refresh token em formato bruto (ex: JWT)
     * @return subject (ex: e-mail ou id do usuário) extraído do token
     *
     * @throws RuntimeException caso o token seja inválido, expirado
     *                          ou não possa ser processado
     */
    String validateRefreshTokenAndGetSubject(String token);
}
