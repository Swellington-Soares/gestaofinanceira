package dev.suel.mstransactionapi.application.exception;

public class RegisteredCustomerOperationException extends RuntimeException {
    public RegisteredCustomerOperationException(String message) {
        super(message);
    }
}
