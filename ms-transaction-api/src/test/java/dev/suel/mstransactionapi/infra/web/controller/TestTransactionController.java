package dev.suel.mstransactionapi.infra.web.controller;

import dev.suel.mstransactionapi.application.usecase.*;
import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.dto.TransactionCreatedResponseDto;
import dev.suel.mstransactionapi.dto.TransactionDetailResponse;
import dev.suel.mstransactionapi.dto.UserTokenInfo;
import dev.suel.mstransactionapi.infra.mapper.PageMapper;
import dev.suel.mstransactionapi.infra.services.SecurityService;
import dev.suel.mstransactionapi.infra.web.dto.TransactionCreateRequest;
import dev.suel.mstransactionapi.infra.web.dto.TransactionTransferCreateRequest;
import dev.suel.mstransactionapi.infra.web.dto.TransactionWithdrawCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

class TestTransactionController {

    private SecurityService securityService;
    private PageMapper pageMapper;
    private GetAllTransactionUseCase getAllTransactionUseCase;
    private DeleteTransactionUseCase deleteTransactionUseCase;
    private ShowTransactionDetailUseCase showTransactionDetailUseCase;
    private CreateDepositTransactionUseCase createDepositTransactionUseCase;
    private CreateWithDrawTransactionUseCase createWithdrawTransactionUseCase;
    private CreateTransferTransactionUseCase createTransferTransactionUseCase;
    private CreateCustomTransactionUseCase createCustomTransactionUseCase;

    private TransactionController controller;

    @BeforeEach
    void setup() {
        securityService = mock(SecurityService.class);
        pageMapper = mock(PageMapper.class);
        getAllTransactionUseCase = mock(GetAllTransactionUseCase.class);
        deleteTransactionUseCase = mock(DeleteTransactionUseCase.class);
        showTransactionDetailUseCase = mock(ShowTransactionDetailUseCase.class);
        createDepositTransactionUseCase = mock(CreateDepositTransactionUseCase.class);
        createWithdrawTransactionUseCase = mock(CreateWithDrawTransactionUseCase.class);
        createTransferTransactionUseCase = mock(CreateTransferTransactionUseCase.class);
        createCustomTransactionUseCase =  mock(CreateCustomTransactionUseCase.class);

        controller = new TransactionController(
                securityService,
                pageMapper,
                getAllTransactionUseCase,
                deleteTransactionUseCase,
                showTransactionDetailUseCase,
                createDepositTransactionUseCase,
                createWithdrawTransactionUseCase,
                createTransferTransactionUseCase,
                createCustomTransactionUseCase
        );
    }

    @Test
    void shouldGetAllTransactions() {
        Authentication authentication = mock(Authentication.class);
        Pageable pageable = mock(Pageable.class);

        UserTokenInfo owner = new UserTokenInfo("a@b.com", 10L, "User");
        PageDataDomain pageDataDomain = new PageDataDomain(0, 10, List.of());

        PaginatedResponse<TransactionDetailResponse> expected = new PaginatedResponse<>(
                List.of(),
                0, 1, 0, 10,
                false, false
        );

        given(securityService.getOwner(authentication)).willReturn(owner);
        given(pageMapper.pageableToPageDataDomain(pageable)).willReturn(pageDataDomain);
        given(getAllTransactionUseCase.execute(pageDataDomain, owner.id())).willReturn(expected);

        ResponseEntity<PaginatedResponse<TransactionDetailResponse>> response =
                controller.getAll(pageable, authentication);

        then(securityService).should().getOwner(authentication);
        then(pageMapper).should().pageableToPageDataDomain(pageable);
        then(getAllTransactionUseCase).should().execute(pageDataDomain, owner.id());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    void shouldDeleteTransaction() {
        Authentication authentication = mock(Authentication.class);
        UUID transactionId = UUID.randomUUID();

        UserTokenInfo owner = new UserTokenInfo("a@b.com", 10L, "User");

        given(securityService.getOwner(authentication)).willReturn(owner);
        willDoNothing().given(deleteTransactionUseCase).execute(transactionId, owner.id());

        ResponseEntity<Void> response = controller.delete(transactionId, authentication);

        then(securityService).should().getOwner(authentication);
        then(deleteTransactionUseCase).should().execute(transactionId, owner.id());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void shouldShowTransactionDetail() {
        Authentication authentication = mock(Authentication.class);
        UUID transactionId = UUID.randomUUID();

        UserTokenInfo owner = new UserTokenInfo("a@b.com", 10L, "User");
        TransactionDetailResponse expected = mock(TransactionDetailResponse.class);

        given(securityService.getOwner(authentication)).willReturn(owner);
        given(showTransactionDetailUseCase.execute(transactionId, owner.id())).willReturn(expected);

        ResponseEntity<TransactionDetailResponse> response = controller.show(transactionId, authentication);

        then(securityService).should().getOwner(authentication);
        then(showTransactionDetailUseCase).should().execute(transactionId, owner.id());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(expected);
    }

    @Test
    void shouldCreateDepositTransaction() {
        Authentication authentication = mock(Authentication.class);
        TransactionCreateRequest request = mock(TransactionCreateRequest.class);

        UserTokenInfo owner = new UserTokenInfo("a@b.com", 10L, "User");
        TransactionCreatedResponseDto expected = mock(TransactionCreatedResponseDto.class);

        given(securityService.getOwner(authentication)).willReturn(owner);
        given(createDepositTransactionUseCase.execute(owner.id(), request)).willReturn(expected);

        ResponseEntity<TransactionCreatedResponseDto> response = controller.deposit(request, authentication);

        then(securityService).should().getOwner(authentication);
        then(createDepositTransactionUseCase).should().execute(owner.id(), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(expected);
    }

    @Test
    void shouldCreateWithdrawTransaction() {
        Authentication authentication = mock(Authentication.class);
        TransactionWithdrawCreateRequest request = mock(TransactionWithdrawCreateRequest.class);

        UserTokenInfo owner = new UserTokenInfo("a@b.com", 10L, "User");
        TransactionCreatedResponseDto expected = mock(TransactionCreatedResponseDto.class);

        given(securityService.getOwner(authentication)).willReturn(owner);
        given(createWithdrawTransactionUseCase.execute(owner.id(), request)).willReturn(expected);

        ResponseEntity<TransactionCreatedResponseDto> response = controller.withdraw(request, authentication);

        then(securityService).should().getOwner(authentication);
        then(createWithdrawTransactionUseCase).should().execute(owner.id(), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(expected);
    }

    @Test
    void shouldCreateTransferTransaction() {
        Authentication authentication = mock(Authentication.class);
        TransactionTransferCreateRequest request = mock(TransactionTransferCreateRequest.class);

        UserTokenInfo owner = new UserTokenInfo("a@b.com", 10L, "User");
        TransactionCreatedResponseDto expected = mock(TransactionCreatedResponseDto.class);

        given(securityService.getOwner(authentication)).willReturn(owner);
        given(createTransferTransactionUseCase.execute(owner.id(), request)).willReturn(expected);

        ResponseEntity<TransactionCreatedResponseDto> response = controller.transfer(request, authentication);

        then(securityService).should().getOwner(authentication);
        then(createTransferTransactionUseCase).should().execute(owner.id(), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(expected);
    }
}
