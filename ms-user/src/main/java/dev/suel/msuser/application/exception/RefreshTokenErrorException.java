package dev.suel.msuser.application.exception;

public class RefreshTokenErrorException extends RuntimeException {
    public RefreshTokenErrorException(String message) {
        super(message);
    }
}
