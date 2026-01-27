package dev.suel.msuser.application.usecase.login;


import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.application.gateway.RefreshTokenServicePort;
import dev.suel.msuser.application.gateway.TokenServicePort;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.dto.TokenInfo;
import dev.suel.msuser.dto.TokenResponse;

import java.util.HashMap;
import java.util.Map;

public class RefreshTokenUserCase {
    private final TokenServicePort tokenServicePort;
    private final RefreshTokenServicePort refreshTokenServicePort;
    private final CustomerServicePort customerServicePort;

    public RefreshTokenUserCase(
            TokenServicePort tokenServicePort,
            RefreshTokenServicePort refreshTokenServicePort, CustomerServicePort customerServicePort) {
        this.tokenServicePort = tokenServicePort;
        this.refreshTokenServicePort = refreshTokenServicePort;
        this.customerServicePort = customerServicePort;
    }

    public TokenResponse execute(String token) {
        String email = refreshTokenServicePort.validateRefreshTokenAndGetSubject(token);
        Customer customer = customerServicePort.findByCustomerEmail(email);
        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId",  customer.getId());
        payload.put("customerName",  customer.getName());
        TokenInfo accessToken = tokenServicePort.generateToken(email, payload);
        TokenInfo refreshToken = refreshTokenServicePort.generateToken(email);
        refreshTokenServicePort.update(email, refreshToken);
        return new TokenResponse(accessToken, refreshToken);
    }
}
