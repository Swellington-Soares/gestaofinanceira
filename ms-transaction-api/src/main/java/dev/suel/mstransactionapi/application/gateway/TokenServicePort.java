package dev.suel.mstransactionapi.application.gateway;

import dev.suel.mstransactionapi.dto.UserTokenInfo;

/**
 * Porta responsável pela validação de tokens de acesso e extração
 * das informações do usuário autenticado.
 * <p>
 * Esta interface pertence à camada de aplicação (Application Layer)
 * e define um contrato para validação de tokens (ex: JWT),
 * abstraindo completamente os detalhes de segurança, biblioteca
 * utilizada ou mecanismo de autenticação.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Validar a autenticidade e integridade do token de acesso</li>
 *     <li>Extrair informações do usuário autenticado</li>
 *     <li>Isolar a camada de domínio de detalhes de segurança</li>
 * </ul>
 *
 * <h2>Fluxo de Uso</h2>
 * <ol>
 *     <li>Receber o token de acesso (ex: Authorization Header)</li>
 *     <li>Validar assinatura, expiração e claims</li>
 *     <li>Retornar os dados do usuário autenticado</li>
 * </ol>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.mstransactionapi.infra.services.JwtTokenService}
 * </ul>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Esta interface não deve conter lógica de infraestrutura</li>
 *     <li>O token deve ser fornecido sem o prefixo {@code Bearer}</li>
 *     <li>Erros de validação devem resultar em exceções apropriadas</li>
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface TokenServicePort {

    /**
     * Valida o token de acesso informado e retorna as informações
     * do usuário autenticado.
     *
     * @param token token de acesso (ex: JWT) sem o prefixo {@code Bearer}
     * @return informações do usuário autenticado {@link UserTokenInfo}
     *
     * @throws RuntimeException caso o token seja inválido, expirado
     *                          ou não possa ser processado.
     */
    UserTokenInfo validateAccessTokenAndGetInfo(String token);
}
