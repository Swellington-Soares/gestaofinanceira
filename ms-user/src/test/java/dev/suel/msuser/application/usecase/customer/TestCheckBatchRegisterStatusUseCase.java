package dev.suel.msuser.application.usecase.customer;

import dev.suel.msuser.application.exception.ResourceNotFoundException;
import dev.suel.msuser.application.gateway.BatchRegisterCustomerPort;
import dev.suel.msuser.application.usecase.customer.CheckBatchRegisterStatusUseCase;
import dev.suel.msuser.domain.FileUploadStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class TestCheckBatchRegisterStatusUseCase {
    private BatchRegisterCustomerPort batchRegisterCustomerPort;
    private CheckBatchRegisterStatusUseCase useCase;

    @BeforeEach
    void setUp() {
        batchRegisterCustomerPort = mock(BatchRegisterCustomerPort.class);
        useCase = new CheckBatchRegisterStatusUseCase(batchRegisterCustomerPort);
    }

    @Test
    void deveRetornarStatusQuandoExistir() {

        String id = "123";
        FileUploadStatus status = FileUploadStatus.withId(id);

        given(batchRegisterCustomerPort.checkStatus(id))
                .willReturn(status);


        FileUploadStatus result = useCase.execute(id);


        assertNotNull(result);
        assertEquals(status, result);
        then(batchRegisterCustomerPort)
                .should()
                .checkStatus(id);
    }

    @Test
    void deveLancarExcecaoQuandoStatusNaoExistir() {

        String id = "999";

        given(batchRegisterCustomerPort.checkStatus(id))
                .willReturn(null);


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> useCase.execute(id)
        );

        assertTrue(exception.getMessage().contains(id));
        then(batchRegisterCustomerPort)
                .should()
                .checkStatus(id);
    }
}
