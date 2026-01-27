package dev.suel.mstransactionapi.domain;

public record UserTokenInfo(
        String email,
        Long id,
        String name
) {
}
