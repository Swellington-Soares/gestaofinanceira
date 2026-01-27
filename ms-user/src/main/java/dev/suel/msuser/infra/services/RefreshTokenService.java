package dev.suel.msuser.infra.services;


import com.google.common.hash.Hashing;
import dev.suel.msuser.application.exception.TokenErrorException;
import dev.suel.msuser.application.gateway.RefreshTokenServicePort;
import dev.suel.msuser.domain.entity.RefreshToken;
import dev.suel.msuser.dto.TokenInfo;
import dev.suel.msuser.infra.configuration.TokenConfigProperty;
import dev.suel.msuser.infra.persistence.entity.RefreshTokenEntity;
import dev.suel.msuser.infra.persistence.repository.RefreshTokenEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenServicePort {
    private final RefreshTokenEntityRepository refreshTokenRepository;
    private final TokenConfigProperty tokenConfigProperty;

    @Override
    @Transactional
    public void update(String email, TokenInfo refreshToken) {
        refreshTokenRepository.deleteByEmail(email);
        refreshTokenRepository.flush();
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(refreshToken.token())
                .createdAt(refreshToken.createAt())
                .expiresIn(refreshToken.expiresIn())
                .email(email)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public TokenInfo generateToken(String email) {
        String uuid = UUID.randomUUID().toString();
        String token = Hashing.sha256().hashString(uuid + email, StandardCharsets.UTF_8).toString();
        Instant createdAt = Instant.now();
        Instant expiredIn = createdAt.plus(tokenConfigProperty.getRefreshTokenValidityMinutes(), ChronoUnit.MINUTES);
        return new TokenInfo(token, createdAt, expiredIn);
    }

    @Override
    @Transactional(readOnly = true)
    public String validateRefreshTokenAndGetSubject(String token) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenErrorException("Refresh Token Inválido."));
        RefreshToken refreshTokenModel = new RefreshToken(
                refreshTokenEntity.getToken(),
                refreshTokenEntity.getEmail(),
                refreshTokenEntity.getExpiresIn(),
                refreshTokenEntity.getCreatedAt()

        );
        if (refreshTokenModel.isExpired())
            throw new TokenErrorException("Refresh Token Expirado.");
        return refreshTokenModel.getEmail();
    }
}
