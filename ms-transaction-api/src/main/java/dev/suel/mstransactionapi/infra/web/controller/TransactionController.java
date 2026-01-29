package dev.suel.mstransactionapi.infra.web.controller;


import dev.suel.mstransactionapi.application.usecase.*;
import dev.suel.mstransactionapi.domain.*;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;
import dev.suel.mstransactionapi.dto.TransactionDetailResponse;
import dev.suel.mstransactionapi.dto.UserTokenInfo;
import dev.suel.mstransactionapi.infra.mapper.PageMapper;
import dev.suel.mstransactionapi.infra.services.SecurityService;
import dev.suel.mstransactionapi.infra.web.dto.TransactionCreateRequest;
import dev.suel.mstransactionapi.infra.web.dto.TransactionCustomCreateRequest;
import dev.suel.mstransactionapi.infra.web.dto.TransactionTransferCreateRequest;
import dev.suel.mstransactionapi.infra.web.dto.TransactionWithdrawCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final SecurityService securityService;
    private final PageMapper pageMapper;
    private final GetAllTransactionUseCase getAllTransactionUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;
    private final ShowTransactionDetailUseCase showTransactionDetailUseCase;
    private final CreateDepositTransactionUseCase createDepositTransactionUseCase;
    private final CreateWithDrawTransactionUseCase createWithdrawTransactionUseCase;
    private final CreateTransferTransactionUseCase createTransferTransactionUseCase;
    private final CreateCustomTransactionUseCase createCustomTransactionUseCase;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaginatedResponse<TransactionDetailResponse>> getAll(Pageable pageable, Authentication authentication) {
        UserTokenInfo owner = securityService.getOwner(authentication);
        PageDataDomain pageDataDomain = pageMapper.pageableToPageDataDomain(pageable);
        return ResponseEntity.ok(getAllTransactionUseCase.execute(pageDataDomain, owner.id()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
        UserTokenInfo owner = securityService.getOwner(authentication);
        deleteTransactionUseCase.execute(id, owner.id());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TransactionDetailResponse> show(@PathVariable UUID id, Authentication authentication) {
        UserTokenInfo owner = securityService.getOwner(authentication);
        return ResponseEntity.ok(showTransactionDetailUseCase.execute(id, owner.id()));
    }

    @PostMapping("/deposit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TransactionCreatedResponseDto> deposit(
            @RequestBody @Valid TransactionCreateRequest data,
            Authentication authentication) {
        UserTokenInfo owner = securityService.getOwner(authentication);
        return ResponseEntity.ok(createDepositTransactionUseCase.execute(owner.id(), data));
    }

    @PostMapping("/withdraw")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TransactionCreatedResponseDto> withdraw(
            @RequestBody @Valid TransactionWithdrawCreateRequest data,
            Authentication authentication) {
        UserTokenInfo owner = securityService.getOwner(authentication);
        return ResponseEntity.ok(createWithdrawTransactionUseCase.execute(owner.id(), data));
    }

    @PostMapping("/transfer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TransactionCreatedResponseDto> transfer(
            @RequestBody @Valid TransactionTransferCreateRequest data,
            Authentication authentication
    ) {
        UserTokenInfo owner = securityService.getOwner(authentication);
        return ResponseEntity.ok(createTransferTransactionUseCase.execute(owner.id(), data));
    }

    @PostMapping("/custom")
    public ResponseEntity<TransactionCreatedResponseDto> custom(
            @RequestBody @Valid TransactionCustomCreateRequest data,
            Authentication authentication
    ) {
        UserTokenInfo owner = securityService.getOwner(authentication);
        return ResponseEntity.ok(createCustomTransactionUseCase.execute(owner.id(), data));
    }

}
