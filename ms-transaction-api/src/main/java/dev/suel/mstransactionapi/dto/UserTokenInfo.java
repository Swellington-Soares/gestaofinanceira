package dev.suel.mstransactionapi.dto;

public record UserTokenInfo(
        String email,
        Long id,
        String name
) {
}
