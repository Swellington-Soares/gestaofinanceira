package dev.suel.msuser.application.gateway;

import dev.suel.msuser.domain.FileUploadStatus;

import java.io.BufferedInputStream;
import java.io.IOException;

public interface BatchRegisterCustomerPort {
    FileUploadStatus checkStatus(String id);
    String getHashCode(BufferedInputStream stream) throws IOException;
    void process(FileUploadStatus fileUploadStatus, BufferedInputStream stream);
}
