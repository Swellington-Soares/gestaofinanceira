package dev.suel.msuser.application.usecase.login;


import dev.suel.msuser.application.gateway.AuthenticationServicePort;
import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.application.gateway.RefreshTokenServicePort;
import dev.suel.msuser.application.gateway.TokenServicePort;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.dto.TokenInfo;
import dev.suel.msuser.dto.TokenResponse;
import dev.suel.msuser.infra.web.dto.LoginRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginUserCase {

    private final AuthenticationServicePort authenticationServicePort;
    private final TokenServicePort tokenServicePort;
    private final RefreshTokenServicePort refreshTokenServicePort;
    private final CustomerServicePort customerServicePort;


    public LoginUserCase(
            AuthenticationServicePort authenticationServicePort,
            TokenServicePort tokenServicePort,
            RefreshTokenServicePort refreshTokenServicePort,
            CustomerServicePort customerServicePort) {
        this.authenticationServicePort = authenticationServicePort;
        this.tokenServicePort = tokenServicePort;
        this.refreshTokenServicePort = refreshTokenServicePort;
        this.customerServicePort = customerServicePort;
    }

    public TokenResponse execute(LoginRequest request) {
        authenticationServicePort.authenticate(request.email(), request.password());
        Customer customer = customerServicePort.findByCustomerEmail(request.email());
        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId",  customer.getId());
        payload.put("customerName",  customer.getName());
        TokenInfo accessToken = tokenServicePort.generateToken(request.email(), payload);
        TokenInfo refreshToken = refreshTokenServicePort.generateToken(request.email());
        refreshTokenServicePort.update(request.email(), refreshToken);
        return new TokenResponse(accessToken, refreshToken);
    }
}
