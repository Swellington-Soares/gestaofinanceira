package dev.suel.msuser.infra.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.suel.msuser.application.gateway.TokenServicePort;
import dev.suel.msuser.dto.TokenInfo;
import dev.suel.msuser.infra.configuration.TokenConfigProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenServicePort {

    private final TokenConfigProperty tokenConfigProperty;

    private Algorithm signatureAlgorithm() {
        return Algorithm.HMAC256(tokenConfigProperty.getSignKey());
    }

    @Override
    public TokenInfo generateToken(String email, Map<String, Object> payload) {
        Instant createdAt = Instant.now();
        Instant expiredAt = createdAt.plus(tokenConfigProperty.getAccessTokenValidityMinutes(), ChronoUnit.MINUTES);
        String token = JWT.create()
                .withPayload(payload)
                .withIssuer(tokenConfigProperty.getIssuer())
                .withIssuedAt(createdAt)
                .withExpiresAt(expiredAt)
                .withSubject(email)
                .sign(signatureAlgorithm());
        return new TokenInfo(token, createdAt, expiredAt);
    }

    @Override
    public String validateAccessTokenAndGetSubject(String token) {
        JWTVerifier verifier = JWT.require(signatureAlgorithm())
                .withIssuer(tokenConfigProperty.getIssuer())
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

}
