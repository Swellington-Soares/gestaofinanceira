package dev.suel.msuser.application.gateway;

/**
 * Porta responsável pela codificação e verificação de senhas
 * de usuários no sistema.
 * <p>
 * Esta interface pertence à camada de aplicação (Application Layer)
 * e define um contrato para operações de segurança relacionadas
 * a senhas, desacoplando a lógica de negócio dos detalhes de
 * algoritmos de hashing, bibliotecas ou frameworks utilizados.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 *     <li>Codificar senhas em texto puro</li>
 *     <li>Validar senhas comparando valor puro com valor codificado</li>
 * </ul>
 *
 * <h2>Observações Importantes</h2>
 * <ul>
 *     <li>Implementações devem utilizar algoritmos seguros (ex: BCrypt, Argon2)</li>
 *     <li>Senhas nunca devem ser armazenadas em texto puro</li>
 *     <li>Comparações devem ser seguras contra ataques de timing</li>
 * </ul>
 *
 * <h2>Possíveis Implementações</h2>
 * <ul>
 *     <li>{@link dev.suel.msuser.infra.services.PasswordEncodeService}
 * </ul>
 *
 * @author Swellington Soares
 * @since 1.0
 */
public interface PasswordEncoderPort {

    /**
     * Codifica uma senha em texto puro utilizando o algoritmo definido
     * pela implementação.
     *
     * @param rawPassword senha em texto puro
     * @return senha codificada
     *
     * @throws RuntimeException caso ocorra erro durante a codificação
     */
    String encode(String rawPassword);

    /**
     * Verifica se uma senha em texto puro corresponde a uma senha codificada.
     *
     * @param rawPassword senha em texto puro
     * @param encodedPassword senha previamente codificada
     * @return {@code true} se as senhas corresponderem; {@code false} caso contrário
     *
     * @throws RuntimeException caso ocorra erro durante a verificação
     */
    boolean matches(String rawPassword, String encodedPassword);
}
