package dev.suel.mstransactionapi.infra.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.suel.mstransactionapi.application.gateway.TokenServicePort;
import dev.suel.mstransactionapi.dto.UserTokenInfo;
import dev.suel.mstransactionapi.infra.configuration.TokenConfigProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenServicePort {
    private final TokenConfigProperty tokenConfigProperty;

    private Algorithm signatureAlgorithm() {
        return Algorithm.HMAC256(tokenConfigProperty.getSignKey());
    }

    @Override
    public UserTokenInfo validateAccessTokenAndGetInfo(String token) {
        JWTVerifier verifier = JWT.require(signatureAlgorithm())
                .withIssuer(tokenConfigProperty.getIssuer())
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return new UserTokenInfo(
                decodedJWT.getSubject(),
                decodedJWT.getClaim("customerId").asLong(),
                decodedJWT.getClaim("customerName").asString());
    }
}
