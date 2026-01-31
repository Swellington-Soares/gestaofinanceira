package dev.suel.mstransactionapi.infra.web.controller;


import dev.suel.mstransactionapi.dto.TransactionDetailResponse;
import dev.suel.mstransactionapi.infra.mapper.TransactionMapper;
import dev.suel.mstransactionapi.infra.persistence.TransactionAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


/**
 * Este controller é usado apenas para o propósito de desenvolvimento, não o exponha em produção.
 */

@Profile("dev")
@RestController
@RequestMapping("/api/v1/transactions/admin")
@RequiredArgsConstructor
public class TransactionAdminController {

    private final TransactionAdminRepository repository;
    private final TransactionMapper mapper;

    @GetMapping
    Page<TransactionDetailResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::transactionEntityToModel)
                .map(mapper::modelToResponse);
    }

    @GetMapping("/{transaction_id}")
    ResponseEntity<TransactionDetailResponse> getOne(@PathVariable UUID transaction_id) {
        try {
            return ResponseEntity.ok(repository.findById(transaction_id)
                    .map(mapper::transactionEntityToModel)
                    .map(mapper::modelToResponse)
                    .orElseThrow());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{transaction_id}")
    ResponseEntity<Void> delete(@PathVariable UUID transaction_id) {
        try {
            repository.deleteById(transaction_id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
