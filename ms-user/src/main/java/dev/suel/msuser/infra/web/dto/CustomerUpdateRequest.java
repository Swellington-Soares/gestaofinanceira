package dev.suel.msuser.infra.web.dto;

public record CustomerUpdateRequest(
        String name,
        String password,
        String newPassword
) {
}
