package dev.suel.mstransactionapi.infra.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.suel.mstransactionapi.application.gateway.TokenServicePort;
import dev.suel.mstransactionapi.dto.UserTokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final TokenServicePort tokenService;

    public JwtSecurityFilter(TokenServicePort tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            try {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserTokenInfo tokenInfo = tokenService.validateAccessTokenAndGetInfo(token);
                    AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(tokenInfo.email(),
                            token,
                            List.of(new SimpleGrantedAuthority("USER")));
                    auth.setDetails(tokenInfo);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JWTVerificationException exception) {
                log.info("JWT inválido: {}", exception.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        return header != null && header.startsWith("Bearer ") ? header.substring(7) : null;
    }
}
