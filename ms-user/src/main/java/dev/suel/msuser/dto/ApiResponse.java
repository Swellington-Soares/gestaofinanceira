package dev.suel.msuser.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        String path,
        Map<String, String> errors
) {
}
