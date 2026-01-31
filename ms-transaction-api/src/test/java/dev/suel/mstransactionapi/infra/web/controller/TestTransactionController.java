package dev.suel.mstransactionapi.infra.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.suel.mstransactionapi.application.gateway.CustomerCanExecuteActionPort;
import dev.suel.mstransactionapi.application.usecase.*;
import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.dto.TransactionDetailResponse;
import dev.suel.mstransactionapi.dto.UserTokenInfo;
import dev.suel.mstransactionapi.infra.mapper.PageMapper;
import dev.suel.mstransactionapi.infra.services.SecurityService;
import dev.suel.mstransactionapi.infra.web.dto.TransactionCreateRequest;
import dev.suel.mstransactionapi.infra.web.dto.TransactionCustomCreateRequest;
import dev.suel.mstransactionapi.infra.web.dto.TransactionTransferCreateRequest;
import dev.suel.mstransactionapi.infra.web.dto.TransactionWithdrawCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TransactionController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = EnableFeignClients.class
        )
)
@TestPropertySource(properties = {
        "services.external.ms-api-check=http://localhost"
})
@Import(TestTransactionController.MethodSecurityTestConfig.class)
@ContextConfiguration(classes = {TransactionController.class})
class TestTransactionController {

    @Configuration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig {
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    SecurityService securityService;

    @MockitoBean
    PageMapper pageMapper;

    @MockitoBean
    GetAllTransactionUseCase getAllTransactionUseCase;

    @MockitoBean
    DeleteTransactionUseCase deleteTransactionUseCase;

    @MockitoBean
    ShowTransactionDetailUseCase showTransactionDetailUseCase;

    @MockitoBean
    CreateDepositTransactionUseCase createDepositTransactionUseCase;

    @MockitoBean
    CreateWithDrawTransactionUseCase createWithdrawTransactionUseCase;

    @MockitoBean
    CreateTransferTransactionUseCase createTransferTransactionUseCase;

    @MockitoBean
    CreateCustomTransactionUseCase createCustomTransactionUseCase;

    @MockitoBean(name = "CustomerCanExecuteService")
    CustomerCanExecuteActionPort customerCanExecuteService;

    private static final Long OWNER_ID = 1L;
    private static final UUID TX_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private UserTokenInfo owner() {
        return new UserTokenInfo("s@email.com", OWNER_ID, "Hello");
    }

    private PageDataDomain pageDomain() {
        return new PageDataDomain(0, 10, List.of(new PageDataDomain.SortField("createdDate", "DESC")));
    }

    private <T> PaginatedResponse<T> pageResponse(List<T> data) {
        return new PaginatedResponse<>(
                data,
                0,
                1,
                data.size(),
                10,
                false,
                false
        );
    }

    private String json(Object value) throws Exception {
        return objectMapper.writeValueAsString(value);
    }

    private TransactionCreateRequest validDeposit() {
        throw new UnsupportedOperationException("Preencha com um request válido do seu DTO TransactionCreateRequest");
    }

    private TransactionWithdrawCreateRequest validWithdraw() {
        throw new UnsupportedOperationException("Preencha com um request válido do seu DTO TransactionWithdrawCreateRequest");
    }

    private TransactionTransferCreateRequest validTransfer() {
        throw new UnsupportedOperationException("Preencha com um request válido do seu DTO TransactionTransferCreateRequest");
    }

    private TransactionCustomCreateRequest validCustom() {
        throw new UnsupportedOperationException("Preencha com um request válido do seu DTO TransactionCustomCreateRequest");
    }

    @Nested
    @DisplayName("GET /api/v1/transactions")
    class GetAll {

        @Test
        void shouldReturn200WhenAllowedAndAuthenticated() throws Exception {
            PageDataDomain pageDataDomain = pageDomain();
            PaginatedResponse<TransactionDetailResponse> response = pageResponse(List.of());

            given(customerCanExecuteService.isAllowed()).willReturn(true);
            given(securityService.getOwner(any())).willReturn(owner());
            given(pageMapper.pageableToPageDataDomain(any(Pageable.class))).willReturn(pageDataDomain);
            given(getAllTransactionUseCase.execute(eq(pageDataDomain), eq(OWNER_ID))).willReturn(response);

            mockMvc.perform(
                            get("/api/v1/transactions")
                                    .param("page", "0")
                                    .param("size", "10")
                                    .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.currentPage").value(0));

            then(getAllTransactionUseCase).should().execute(pageDataDomain, OWNER_ID);
        }

        @Test
        void shouldReturn401WhenNotAuthenticated() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(get("/api/v1/transactions"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldReturn403WhenCustomerCannotExecute() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(false);

            mockMvc.perform(
                            get("/api/v1/transactions")
                                    .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isForbidden());

            then(getAllTransactionUseCase).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/transactions/{id}")
    class Show {

        @Test
        void shouldReturn200WhenAllowedAndAuthenticated() throws Exception {
            TransactionDetailResponse detail = mock(TransactionDetailResponse.class);

            given(customerCanExecuteService.isAllowed()).willReturn(true);
            given(securityService.getOwner(any())).willReturn(owner());
            given(showTransactionDetailUseCase.execute(eq(TX_ID), eq(OWNER_ID))).willReturn(detail);

            mockMvc.perform(
                            get("/api/v1/transactions/{id}", TX_ID)
                                    .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isOk());

            then(showTransactionDetailUseCase).should().execute(TX_ID, OWNER_ID);
        }

        @Test
        void shouldReturn400WhenInvalidUuid() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(
                            get("/api/v1/transactions/{id}", "invalid-uuid")
                                    .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isBadRequest());

            then(showTransactionDetailUseCase).shouldHaveNoInteractions();
        }

        @Test
        void shouldReturn401WhenNotAuthenticated() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(get("/api/v1/transactions/{id}", TX_ID))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldReturn403WhenCustomerCannotExecute() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(false);

            mockMvc.perform(
                            get("/api/v1/transactions/{id}", TX_ID)
                                    .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isForbidden());

            then(showTransactionDetailUseCase).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/transactions/{id}")
    class Delete {

        @Test
        void shouldReturn403WhenCustomerCannotExecute() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(false);

            mockMvc.perform(
                            delete("/api/v1/transactions/{id}", TX_ID)
                                    .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isForbidden());

            then(deleteTransactionUseCase).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("POST /api/v1/transactions/deposit")
    class Deposit {



        @Test
        void shouldReturn403WhenCustomerCannotExecute() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(false);

            mockMvc.perform(
                            post("/api/v1/transactions/deposit")
                                    .with(user("user").roles("USER"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{}")
                    )
                    .andExpect(status().isForbidden());

            then(createDepositTransactionUseCase).shouldHaveNoInteractions();
        }
    }



}
