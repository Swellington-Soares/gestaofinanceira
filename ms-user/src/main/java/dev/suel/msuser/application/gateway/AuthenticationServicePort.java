package dev.suel.msuser.application.gateway;

public interface AuthenticationServicePort {
    void authenticate(String email, String password);
}
