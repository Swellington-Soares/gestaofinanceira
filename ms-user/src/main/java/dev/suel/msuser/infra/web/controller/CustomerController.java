package dev.suel.msuser.infra.web.controller;


import dev.suel.msuser.application.usecase.customer.*;
import dev.suel.msuser.domain.FileUploadStatus;
import dev.suel.msuser.domain.PageDataDomain;
import dev.suel.msuser.domain.PaginatedResponse;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.dto.CustomerInfoResponse;
import dev.suel.msuser.dto.FileUploadResponse;
import dev.suel.msuser.infra.mapper.CustomerMapper;
import dev.suel.msuser.infra.mapper.PageMapper;
import dev.suel.msuser.infra.web.dto.CustomerCreateRequest;
import dev.suel.msuser.infra.web.dto.CustomerUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final ProcessCustomerRegisterByUploadedFileUseCase processCustomerRegisterByUploadedFileUseCase;
    private final CheckBatchRegisterStatusUseCase checkBatchRegisterStatusUseCase;
    private final ListAllCustomerPaginatedUseCase listAllCustomerPaginatedUseCase;
    private final RegisterNewCustomerUseCase registerNewCustomerUseCase;
    private final RemoveCustomerByIdUseCase removeCustomerByIdUseCase;
    private final UpdateCustomerByIdUseCase updateCustomerByIdUseCase;
    private final FindCustomerByIdUseCase findCustomerByIdUseCase;
    private final CustomerMapper customerMapper;
    private final PageMapper pageMapper;

    @PostMapping(path = "/batch-register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> upload(@RequestParam MultipartFile file) {
        if (file.isEmpty())
            throw new IllegalArgumentException("Arquivo inválido.");

        try {
            FileUploadResponse info = processCustomerRegisterByUploadedFileUseCase.execute(
                    new BufferedInputStream(file.getInputStream())
            );
            return ResponseEntity.accepted().body(info);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping("/batch-register/{id}")
    public ResponseEntity<FileUploadStatus> checkUploadStatus(@PathVariable String id) {
        return ResponseEntity.ok(checkBatchRegisterStatusUseCase.execute(id));
    }

    @PostMapping
    ResponseEntity<CustomerInfoResponse> create(
            @RequestBody @Valid CustomerCreateRequest data) {
        Customer customer = registerNewCustomerUseCase.execute(data);
        CustomerInfoResponse response = customerMapper.modelToResponse(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<CustomerInfoResponse>> getAllUsers(Pageable pageable) {
        PageDataDomain pageDataDomain = pageMapper.pageableToPageDataDomain(pageable);
        return ResponseEntity.ok(listAllCustomerPaginatedUseCase.execute(pageDataDomain));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() && @security.isOwner(#id, authentication)")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        removeCustomerByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() && @security.isOwner(#id, authentication)")
    ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody @Valid CustomerUpdateRequest data) {
        updateCustomerByIdUseCase.execute(id, data);
        return ResponseEntity.accepted().build();
    }



    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() && @security.isOwner(#id, authentication)")
    ResponseEntity<CustomerInfoResponse> read(@PathVariable Long id) {
        Customer customer = findCustomerByIdUseCase.execute(id);
        CustomerInfoResponse response = customerMapper.modelToResponse(customer);
        return ResponseEntity.ok(response);
    }
}
