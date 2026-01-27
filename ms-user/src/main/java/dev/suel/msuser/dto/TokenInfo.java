package dev.suel.msuser.dto;

import java.time.Instant;

public record TokenInfo(
        String token,
        Instant createAt,
        Instant expiresIn
) {
}
