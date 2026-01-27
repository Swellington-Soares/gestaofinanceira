package dev.suel.msuser.application.usecase.customer;

import dev.suel.msuser.application.gateway.BatchRegisterCustomerPort;
import dev.suel.msuser.domain.FileUploadStatus;
import dev.suel.msuser.dto.FileUploadResponse;

import java.io.BufferedInputStream;
import java.io.IOException;

public class ProcessCustomerRegisterByUploadedFileUseCase {

    private final BatchRegisterCustomerPort batchRegisterCustomerPort;

    public ProcessCustomerRegisterByUploadedFileUseCase(BatchRegisterCustomerPort batchRegisterCustomerPort) {
        this.batchRegisterCustomerPort = batchRegisterCustomerPort;
    }

    public FileUploadResponse execute(BufferedInputStream stream) throws IOException {
        String fileHash = batchRegisterCustomerPort.getHashCode( stream );
        FileUploadStatus fileUploadStatus = FileUploadStatus.withId(fileHash);
        fileUploadStatus.setStatus(FileUploadStatus.Status.STATUS_PROCESSING);

        batchRegisterCustomerPort.process(fileUploadStatus, stream);

        return new FileUploadResponse(fileHash, "Processamento iniciado.", fileUploadStatus.getCreatedAt());
    }
}
