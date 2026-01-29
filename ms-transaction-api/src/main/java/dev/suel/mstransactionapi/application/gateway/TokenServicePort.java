package dev.suel.mstransactionapi.application.gateway;


import dev.suel.mstransactionapi.dto.UserTokenInfo;

public interface TokenServicePort {
    UserTokenInfo validateAccessTokenAndGetInfo(String token);
}
