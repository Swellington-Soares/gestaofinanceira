package dev.suel.msuser.application.usecase.customer;

import dev.suel.msuser.dto.FileUploadResponse;
import dev.suel.msuser.infra.services.BatchRegisterCustomerService;

import java.io.IOException;
import java.io.InputStream;

public class RegisterNewCustomerByFileUseCase {

    private final BatchRegisterCustomerService batchRegisterCustomerService;

    public RegisterNewCustomerByFileUseCase(BatchRegisterCustomerService batchRegisterCustomerService) {
        this.batchRegisterCustomerService = batchRegisterCustomerService;
    }

    public FileUploadResponse execute(String id, InputStream inputStream) throws IOException {

        return null;
    }
}
