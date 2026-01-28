package dev.suel.msuser.application.usecase.customer;

import dev.suel.msuser.application.exception.ResourceNotFoundException;
import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.application.gateway.PasswordEncoderPort;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.domain.valueobject.Password;
import dev.suel.msuser.domain.valueobject.PersonName;
import dev.suel.msuser.infra.web.dto.CustomerUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class TestUpdateCustomerByIdUseCase {

    private CustomerServicePort customerServicePort;
    private PasswordEncoderPort passwordEncoderPort;
    private UpdateCustomerByIdUseCase useCase;

    private static final Password STRONG_PASSWORD = Password.of("123456!!AAaa");
    private static final Password STRONG_NEWPASSWORD = Password.of("123456!!AAaaCC");

    @BeforeEach
    void setUp() {
        customerServicePort = mock(CustomerServicePort.class);
        passwordEncoderPort = mock(PasswordEncoderPort.class);
        useCase = new UpdateCustomerByIdUseCase(customerServicePort, passwordEncoderPort);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistir() {
        Long id = 1L;
        CustomerUpdateRequest request = mock(CustomerUpdateRequest.class);

        given(customerServicePort.findCustomerById(id))
                .willReturn(null);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> useCase.execute(id, request)
        );

        assertTrue(exception.getMessage().contains(id.toString()));
        then(customerServicePort).should().findCustomerById(id);
        then(customerServicePort).shouldHaveNoMoreInteractions();
    }

    @Test
    void deveAtualizarNomeQuandoForDiferente() {
        Long id = 1L;
        Customer customer = mock(Customer.class);
        CustomerUpdateRequest request = mock(CustomerUpdateRequest.class);

        given(customerServicePort.findCustomerById(id))
                .willReturn(customer);

        given(request.name())
                .willReturn("Novo Nome");

        given(customer.getName())
                .willReturn(PersonName.of("Nome Antigo").getValue());

        useCase.execute(id, request);

        then(customer).should().setName(PersonName.of("Novo Nome"));
        then(customerServicePort).should().updateCustomer(customer);
    }

    @Test
    void naoDeveAtualizarNomeQuandoForIgual() {
        Long id = 1L;
        Customer customer = mock(Customer.class);
        CustomerUpdateRequest request = mock(CustomerUpdateRequest.class);

        given(customerServicePort.findCustomerById(id))
                .willReturn(customer);

        given(request.name())
                .willReturn("Mesmo Nome");

        given(customer.getName())
                .willReturn(PersonName.of("Mesmo Nome").getValue());

        useCase.execute(id, request);

        then(customerServicePort).should(never()).updateCustomer(any());
    }

    @Test
    void deveLancarExcecaoQuandoPasswordAtualNaoBater() {
        Long id = 1L;
        Customer customer = mock(Customer.class);
        CustomerUpdateRequest request = mock(CustomerUpdateRequest.class);

        given(customerServicePort.findCustomerById(id))
                .willReturn(customer);

        given(request.password())
                .willReturn(STRONG_PASSWORD.getValue());

        given(request.newPassword())
                .willReturn(STRONG_NEWPASSWORD.getValue());

        given(customer.getPassword())
                .willReturn(Password.of(STRONG_NEWPASSWORD.getValue()).getValue());

        given(passwordEncoderPort.matches(STRONG_PASSWORD.getValue(), customer.getPassword()))
                .willReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(id, request)
        );

        assertTrue(exception.getMessage().contains("password atual"));
    }

    @Test
    void deveAlterarSenhaQuandoDadosForemValidos() {
        Long id = 1L;
        Customer customer = mock(Customer.class);
        CustomerUpdateRequest request = mock(CustomerUpdateRequest.class);

        given(customerServicePort.findCustomerById(id))
                .willReturn(customer);

        given(request.password())
                .willReturn(STRONG_PASSWORD.getValue());

        given(request.newPassword())
                .willReturn(STRONG_NEWPASSWORD.getValue());

        given(customer.getPassword())
                .willReturn(STRONG_NEWPASSWORD.getValue());

        given(passwordEncoderPort.matches(STRONG_PASSWORD.getValue(), customer.getPassword()))
                .willReturn(true);

        given(customerServicePort.changeCustomerPassword(customer, STRONG_NEWPASSWORD.getValue()))
                .willReturn(true);

        useCase.execute(id, request);

        then(customerServicePort).should()
                .changeCustomerPassword(customer, STRONG_NEWPASSWORD.getValue());
    }
}
