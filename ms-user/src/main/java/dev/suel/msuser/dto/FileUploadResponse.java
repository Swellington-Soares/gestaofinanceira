package dev.suel.msuser.dto;

import java.time.Instant;

public record FileUploadResponse(
        String id,
        String message,
        Instant startedAt
) {
}
