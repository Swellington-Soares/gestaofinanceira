package dev.suel.mstransactionapi.dto;

import java.time.LocalDate;
import java.util.UUID;

public record DocumentGenerationResponse(
        UUID id,
        LocalDate requestedAt,
        String status,
        String format
) {
}
