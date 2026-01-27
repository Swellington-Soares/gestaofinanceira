package dev.suel.msuser.infra.web.controller;


import dev.suel.msuser.application.usecase.login.LoginUserCase;
import dev.suel.msuser.application.usecase.login.RefreshTokenUserCase;
import dev.suel.msuser.dto.TokenResponse;
import dev.suel.msuser.infra.web.dto.LoginRequest;
import dev.suel.msuser.infra.web.dto.RefreshTokenRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {
    private final LoginUserCase loginUserCase;
    private final RefreshTokenUserCase refreshTokenUserCase;

    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest data) {
        return ResponseEntity.ok(loginUserCase.execute(data));
    }


    @PostMapping("/refresh")
    ResponseEntity<TokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest data) {
        return ResponseEntity.ok(refreshTokenUserCase.execute(data.refreshToken()));
    }
}
