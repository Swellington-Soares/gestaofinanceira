package dev.suel.msuser.application.exception;

public class TokenErrorException extends RuntimeException {
    public TokenErrorException(String message) {
        super(message);
    }
}
