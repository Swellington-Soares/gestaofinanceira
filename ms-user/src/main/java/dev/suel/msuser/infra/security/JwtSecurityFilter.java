package dev.suel.msuser.infra.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.suel.msuser.application.gateway.TokenServicePort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final TokenServicePort tokenService;
    private final UserDetailsService userDetailsService;

    public JwtSecurityFilter(TokenServicePort tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
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
                    String email = tokenService.validateAccessTokenAndGetSubject(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email,
                            token,
                            userDetails.getAuthorities());
                    auth.setDetails(userDetails);
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
