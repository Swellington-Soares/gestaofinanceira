package dev.suel.msuser.application.gateway;


import dev.suel.msuser.dto.TokenInfo;

public interface RefreshTokenServicePort {
    void update(String email, TokenInfo refreshToken);
    TokenInfo generateToken(String email);
    String validateRefreshTokenAndGetSubject(String token);
}
