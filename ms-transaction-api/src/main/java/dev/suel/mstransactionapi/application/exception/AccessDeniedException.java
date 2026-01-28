package dev.suel.mstransactionapi.application.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("Você não tem autorização para acessar este recurso.");
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
