package dev.suel.msuser.application.usecase.customer;


import dev.suel.msuser.application.exception.ResourceNotFoundException;
import dev.suel.msuser.application.gateway.BatchRegisterCustomerPort;
import dev.suel.msuser.domain.FileUploadStatus;

public class CheckBatchRegisterStatusUseCase {

    private final BatchRegisterCustomerPort batchRegisterCustomerPort;

    public CheckBatchRegisterStatusUseCase(BatchRegisterCustomerPort batchRegisterCustomerPort) {
        this.batchRegisterCustomerPort = batchRegisterCustomerPort;
    }

    public FileUploadStatus execute(String id) {
        FileUploadStatus response = batchRegisterCustomerPort.checkStatus(id);
        if (response == null)
            throw new ResourceNotFoundException("Situação do cadastro em lote com ID: " + id + " indisponível.");
        return response;
    }
}
