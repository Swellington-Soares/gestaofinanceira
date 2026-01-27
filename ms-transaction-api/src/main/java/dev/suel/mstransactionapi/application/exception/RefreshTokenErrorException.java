package dev.suel.mstransactionapi.application.exception;

public class RefreshTokenErrorException extends RuntimeException {
    public RefreshTokenErrorException(String message) {
        super(message);
    }
}
