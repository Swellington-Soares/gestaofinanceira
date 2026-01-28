package dev.suel.msuser.application.usecase.login;

import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.application.gateway.RefreshTokenServicePort;
import dev.suel.msuser.application.gateway.TokenServicePort;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.dto.TokenInfo;
import dev.suel.msuser.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class TestRefreshTokenUserCase {

    private TokenServicePort tokenServicePort;
    private RefreshTokenServicePort refreshTokenServicePort;
    private CustomerServicePort customerServicePort;
    private RefreshTokenUserCase useCase;

    @BeforeEach
    void setUp() {
        tokenServicePort = mock(TokenServicePort.class);
        refreshTokenServicePort = mock(RefreshTokenServicePort.class);
        customerServicePort = mock(CustomerServicePort.class);

        useCase = new RefreshTokenUserCase(
                tokenServicePort,
                refreshTokenServicePort,
                customerServicePort
        );
    }

    @Test
    void deveValidarRefreshTokenGerarNovosTokensEAtualizarRefreshToken() {
        String refreshTokenValue = "refresh-token";
        String email = "user@email.com";

        Customer customer = mock(Customer.class);
        TokenInfo accessToken = mock(TokenInfo.class);
        TokenInfo newRefreshToken = mock(TokenInfo.class);

        given(refreshTokenServicePort.validateRefreshTokenAndGetSubject(refreshTokenValue))
                .willReturn(email);

        given(customerServicePort.findByCustomerEmail(email))
                .willReturn(customer);

        given(customer.getId())
                .willReturn(1L);

        given(customer.getName())
                .willReturn("User Name");

        given(tokenServicePort.generateToken(eq(email), any(Map.class)))
                .willReturn(accessToken);

        given(refreshTokenServicePort.generateToken(email))
                .willReturn(newRefreshToken);

        TokenResponse response = useCase.execute(refreshTokenValue);

        assertNotNull(response);
        assertEquals(accessToken, response.accessToken());
        assertEquals(newRefreshToken, response.refreshToken());

        then(refreshTokenServicePort)
                .should()
                .validateRefreshTokenAndGetSubject(refreshTokenValue);

        then(customerServicePort)
                .should()
                .findByCustomerEmail(email);

        then(tokenServicePort)
                .should()
                .generateToken(
                        eq(email),
                        argThat(payload ->
                                payload.get("customerId").equals(1L)
                                        && payload.get("customerName").equals("User Name")
                        )
                );

        then(refreshTokenServicePort)
                .should()
                .generateToken(email);

        then(refreshTokenServicePort)
                .should()
                .update(email, newRefreshToken);
    }
}
