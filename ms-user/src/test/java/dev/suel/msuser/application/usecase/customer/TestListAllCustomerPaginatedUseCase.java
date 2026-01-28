package dev.suel.msuser.application.usecase.customer;

import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.domain.PageDataDomain;
import dev.suel.msuser.domain.PaginatedResponse;
import dev.suel.msuser.dto.CustomerInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class TestListAllCustomerPaginatedUseCase {

    private CustomerServicePort customerServicePort;
    private ListAllCustomerPaginatedUseCase useCase;

    @BeforeEach
    void setUp() {
        customerServicePort = mock(CustomerServicePort.class);
        useCase = new ListAllCustomerPaginatedUseCase(customerServicePort);
    }

    @Test
    void deveRetornarListaPaginadaDeClientes() {
        PageDataDomain pageDataDomain = mock(PageDataDomain.class);
        PaginatedResponse<CustomerInfoResponse> paginatedResponse = mock(PaginatedResponse.class);

        given(customerServicePort.findAll(pageDataDomain))
                .willReturn(paginatedResponse);

        PaginatedResponse<CustomerInfoResponse> result = useCase.execute(pageDataDomain);

        assertEquals(paginatedResponse, result);
        then(customerServicePort).should().findAll(pageDataDomain);
    }
}
