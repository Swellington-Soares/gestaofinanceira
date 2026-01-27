package dev.suel.mstransactionapi.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public record ApiResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        String path,
        Object errors
) {
}
