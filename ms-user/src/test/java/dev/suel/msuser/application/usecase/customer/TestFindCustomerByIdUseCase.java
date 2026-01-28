package dev.suel.msuser.application.usecase.customer;

import dev.suel.msuser.application.exception.ResourceNotFoundException;
import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.application.usecase.customer.FindCustomerByIdUseCase;
import dev.suel.msuser.domain.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class TestFindCustomerByIdUseCase {

    private CustomerServicePort customerServicePort;
    private FindCustomerByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        customerServicePort = mock(CustomerServicePort.class);
        useCase = new FindCustomerByIdUseCase(customerServicePort);
    }

    @Test
    void deveRetornarClienteQuandoExistir() {
        Long id = 1L;
        Customer customer = mock(Customer.class);

        given(customerServicePort.findCustomerById(id))
                .willReturn(customer);

        Customer result = useCase.execute(id);

        assertEquals(customer, result);
        then(customerServicePort).should().findCustomerById(id);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistir() {
        Long id = 99L;

        given(customerServicePort.findCustomerById(id))
                .willReturn(null);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> useCase.execute(id)
        );

        assertTrue(exception.getMessage().contains(id.toString()));
        then(customerServicePort).should().findCustomerById(id);
    }
}
