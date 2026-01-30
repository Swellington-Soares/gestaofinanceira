package dev.suel.msuser.application.usecase.customer;

import dev.suel.msuser.application.gateway.BatchRegisterCustomerPort;
import dev.suel.msuser.domain.FileUploadStatus;
import dev.suel.msuser.dto.FileUploadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.*;

class TestProcessCustomerRegisterByUploadedFileUseCase {

    private BatchRegisterCustomerPort batchRegisterCustomerPort;
    private ProcessCustomerRegisterByUploadedFileUseCase useCase;

    @BeforeEach
    void setUp() {
        batchRegisterCustomerPort = mock(BatchRegisterCustomerPort.class);
        useCase = new ProcessCustomerRegisterByUploadedFileUseCase(batchRegisterCustomerPort);
    }

    @Test
    void deveIniciarProcessamentoDeArquivoComSucesso() throws IOException {
        BufferedInputStream stream =
                new BufferedInputStream(new ByteArrayInputStream("file-content".getBytes()));

        String fileHash = "hash-123";

        given(batchRegisterCustomerPort.getHashCode(stream))
                .willReturn(fileHash);

        FileUploadResponse response = useCase.execute(stream);

        assertNotNull(response);
        assertEquals(fileHash, response.id());
        assertEquals("Processamento iniciado.", response.message());
        assertNotNull(response.startedAt());

        then(batchRegisterCustomerPort).should()
                .getHashCode(stream);

        then(batchRegisterCustomerPort).should()
                .process(
                        argThat(status ->
                                status.getId().equals(fileHash)
                                        && status.getStatus() == FileUploadStatus.Status.STATUS_PROCESSING
                        ),
                        eq(stream)
                );
    }
}
