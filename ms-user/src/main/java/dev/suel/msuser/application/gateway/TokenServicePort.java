package dev.suel.msuser.application.gateway;


import dev.suel.msuser.dto.TokenInfo;

import java.util.Map;

public interface TokenServicePort {
    TokenInfo generateToken(String email, Map<String, Object> payload);
    String validateAccessTokenAndGetSubject(String token);
}
