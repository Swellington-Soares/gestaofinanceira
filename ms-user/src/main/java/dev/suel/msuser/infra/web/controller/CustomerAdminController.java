package dev.suel.msuser.infra.web.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/admin")
@RequiredArgsConstructor
public class CustomerAdminController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<String> delete(@RequestParam(name = "user_id") Long userId) {
        return ResponseEntity.ok(userId.toString());
    }
}
