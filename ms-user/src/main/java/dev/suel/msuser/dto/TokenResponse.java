package dev.suel.msuser.dto;


public record TokenResponse(
        TokenInfo accessToken,
        TokenInfo refreshToken
) {
}
