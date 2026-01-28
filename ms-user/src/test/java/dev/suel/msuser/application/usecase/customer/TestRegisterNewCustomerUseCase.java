package dev.suel.msuser.application.usecase.customer;

import dev.suel.msuser.application.exception.ResourceAlreadyExistsException;
import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.domain.valueobject.Email;
import dev.suel.msuser.domain.valueobject.Password;
import dev.suel.msuser.domain.valueobject.PersonName;
import dev.suel.msuser.infra.web.dto.CustomerCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class TestRegisterNewCustomerUseCase {

    private CustomerServicePort customerServicePort;
    private RegisterNewCustomerUseCase useCase;

    private static final Password STRONG_PASSWORD = Password.of("123456!!AAaa");

    @BeforeEach
    void setUp() {
        customerServicePort = mock(CustomerServicePort.class);
        useCase = new RegisterNewCustomerUseCase(customerServicePort);
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExistir() {
        CustomerCreateRequest request =
                new CustomerCreateRequest("joao@email.com", STRONG_PASSWORD.getValue(),"João Silva");

        given(customerServicePort.existsByEmail(request.email()))
                .willReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> useCase.execute(request)
        );

        assertEquals(
                "Já existe um cliente cadastrado com o mesmo e-mail.",
                exception.getMessage()
        );

        then(customerServicePort).should().existsByEmail(request.email());
        then(customerServicePort).shouldHaveNoMoreInteractions();
    }

    @Test
    void deveRegistrarNovoClienteQuandoEmailNaoExistir() {
        CustomerCreateRequest request =
                new CustomerCreateRequest(
                        "joao@email.com",
                        STRONG_PASSWORD.getValue(),
                        "João Silva"
                );

        Customer savedCustomer = Customer.builder()
                .email(Email.of(request.email()))
                .name(PersonName.of(request.name()))
                .build();

        given(customerServicePort.existsByEmail(request.email()))
                .willReturn(false);

        given(customerServicePort.registerNewCustomer(any(Customer.class)))
                .willReturn(savedCustomer);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

        Customer result = useCase.execute(request);

        assertEquals(savedCustomer, result);

        then(customerServicePort).should().existsByEmail(request.email());
        then(customerServicePort).should().registerNewCustomer(captor.capture());

        Customer captured = captor.getValue();

        assertEquals(request.email(), captured.getEmail());
        assertEquals(request.name().toUpperCase(Locale.ROOT), captured.getName());
    }

}
