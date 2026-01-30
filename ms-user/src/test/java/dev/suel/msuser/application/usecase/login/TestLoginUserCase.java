package dev.suel.msuser.application.usecase.login;

import dev.suel.msuser.application.gateway.AuthenticationServicePort;
import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.application.gateway.RefreshTokenServicePort;
import dev.suel.msuser.application.gateway.TokenServicePort;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.dto.TokenInfo;
import dev.suel.msuser.dto.TokenResponse;
import dev.suel.msuser.infra.web.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.*;

class TestLoginUserCase {

    private AuthenticationServicePort authenticationServicePort;
    private TokenServicePort tokenServicePort;
    private RefreshTokenServicePort refreshTokenServicePort;
    private CustomerServicePort customerServicePort;
    private LoginUserCase useCase;

    @BeforeEach
    void setUp() {
        authenticationServicePort = mock(AuthenticationServicePort.class);
        tokenServicePort = mock(TokenServicePort.class);
        refreshTokenServicePort = mock(RefreshTokenServicePort.class);
        customerServicePort = mock(CustomerServicePort.class);

        useCase = new LoginUserCase(
                authenticationServicePort,
                tokenServicePort,
                refreshTokenServicePort,
                customerServicePort
        );
    }

    @Test
    void deveAutenticarGerarTokensAtualizarRefreshTokenERetornarResposta() {
        LoginRequest request = new LoginRequest("user@email.com", "123456");

        Customer customer = mock(Customer.class);
        TokenInfo accessToken = mock(TokenInfo.class);
        TokenInfo refreshToken = mock(TokenInfo.class);

        given(customerServicePort.findByCustomerEmail(request.email()))
                .willReturn(customer);

        given(customer.getId())
                .willReturn(1L);

        given(customer.getName())
                .willReturn("User Name");

        given(tokenServicePort.generateToken(eq(request.email()), any(Map.class)))
                .willReturn(accessToken);

        given(refreshTokenServicePort.generateToken(request.email()))
                .willReturn(refreshToken);

        TokenResponse response = useCase.execute(request);

        assertNotNull(response);
        assertEquals(accessToken, response.accessToken());
        assertEquals(refreshToken, response.refreshToken());

        then(authenticationServicePort)
                .should()
                .authenticate(request.email(), request.password());

        then(customerServicePort)
                .should()
                .findByCustomerEmail(request.email());

        then(tokenServicePort)
                .should()
                .generateToken(
                        eq(request.email()),
                        argThat(payload ->
                                payload.get("customerId").equals(1L)
                                        && payload.get("customerName").equals("User Name")
                        )
                );

        then(refreshTokenServicePort)
                .should()
                .generateToken(request.email());

        then(refreshTokenServicePort)
                .should()
                .update(request.email(), refreshToken);
    }
}
