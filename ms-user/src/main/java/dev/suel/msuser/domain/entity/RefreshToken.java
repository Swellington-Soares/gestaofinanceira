package dev.suel.msuser.domain.entity;

import java.time.Instant;
import java.util.Objects;

public class RefreshToken {
    private final String token;
    private final String email;
    private final Instant expiresIn;
    private final Instant createdAt;

    public RefreshToken(String token, String email, Instant expiresIn, Instant createdAt) {
        this.token = token;
        this.email = email;
        this.expiresIn = expiresIn;
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public Instant getExpiresIn() {
        return expiresIn;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(getToken(), that.getToken());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getToken());
    }

    public boolean isExpired() {
        return expiresIn.isBefore(Instant.now());
    }
}
