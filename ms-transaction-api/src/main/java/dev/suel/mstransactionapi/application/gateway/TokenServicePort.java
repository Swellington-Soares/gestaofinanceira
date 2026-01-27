package dev.suel.mstransactionapi.application.gateway;


import dev.suel.mstransactionapi.domain.UserTokenInfo;

public interface TokenServicePort {
    UserTokenInfo validateAccessTokenAndGetInfo(String token);
}
