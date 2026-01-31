package dev.suel.msuser.application.gateway;

import dev.suel.msuser.dto.TokenInfo;

import java.util.Map;

/**
 * Porta responsável pela geração e validação de tokens de acesso
 * utilizados no processo de autenticação e autorização.
 * <p>
 * Esta interface pertence à camada de aplicação (Application Layer)
 * e define um contrato para manipulação de access tokens (ex: JWT),
 * desacoplando os casos de uso dos detalhes de segurança, bibliotecas
 * criptográficas ou provedores de identidade.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Gerar tokens de acesso para usuários autenticados</li>
 *     <li>Validar tokens de acesso recebidos pelo sistema</li>
 *     <li>Extrair informações básicas (subject) a partir do token</li>
 * </ul>
 *
 * <h2>Contexto de Uso</h2>
 * <p>
 * Este port é normalmente utilizado após a autenticação do usuário
 * e na validação de requisições protegidas por segurança.
 * </p>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Tokens devem possuir tempo de expiração adequado</li>
 *     <li>O payload pode conter claims adicionais (ex: roles, permissões)</li>
 *     <li>Validações devem considerar assinatura, expiração e integridade</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.msuser.infra.services.JwtTokenService}
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface TokenServicePort {

    /**
     * Gera um token de acesso para o usuário informado,
     * incluindo informações adicionais no payload.
     *
     * @param email identificador do usuário (subject do token)
     * @param payload claims adicionais a serem incluídas no token
     * @return informações do token gerado {@link TokenInfo}
     *
     * @throws RuntimeException caso ocorra erro durante a geração do token
     */
    TokenInfo generateToken(String email, Map<String, Object> payload);

    /**
     * Valida um token de acesso e retorna o identificador (subject)
     * associado ao token.
     *
     * @param token token de acesso em formato bruto (ex: JWT)
     * @return subject extraído do token (ex: e-mail do usuário)
     *
     * @throws RuntimeException caso o token seja inválido, expirado
     *                          ou não possa ser processado
     */
    String validateAccessTokenAndGetSubject(String token);
}
