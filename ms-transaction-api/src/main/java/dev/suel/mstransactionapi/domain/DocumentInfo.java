package dev.suel.mstransactionapi.domain;

import java.util.UUID;

public record DocumentInfo(
        UUID id,
        String url,
        String format
) {
}
