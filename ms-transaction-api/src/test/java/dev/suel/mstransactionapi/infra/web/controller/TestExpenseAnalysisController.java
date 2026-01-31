//package dev.suel.mstransactionapi.infra.web.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import dev.suel.mstransactionapi.application.exception.AccessDeniedException;
//import dev.suel.mstransactionapi.application.gateway.CustomerCanExecuteActionPort;
//import dev.suel.mstransactionapi.application.usecase.GeneratePDFDocumentUseCase;
//import dev.suel.mstransactionapi.application.usecase.TransactionReportUseCase;
//import dev.suel.mstransactionapi.domain.PageDataDomain;
//import dev.suel.mstransactionapi.domain.PaginatedResponse;
//import dev.suel.mstransactionapi.dto.ExpenseByCategory;
//import dev.suel.mstransactionapi.infra.mapper.PageMapper;
//import dev.suel.mstransactionapi.infra.services.SecurityService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
////@WebMvcTest(
////        controllers = ExpenseAnalysisController.class,
////        excludeFilters = {
////                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = EnableFeignClients.class),
////                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Feign.*")
////        }
////)
////@TestPropertySource(properties = {
////        "services.external.ms-api-check=http://localhost",
////        "services.external.ms-api-check=https://localhost"
////})
////@Import(TestExpenseAnalysisController.MethodSecurityTestConfig.class)
//@WebMvcTest(
//        controllers = ExpenseAnalysisController.class,
//        excludeFilters = @ComponentScan.Filter(
//                type = FilterType.ANNOTATION,
//                classes = EnableFeignClients.class
//        )
//)
//@TestPropertySource(properties = {
//        "services.external.ms-api-check=http://localhost"
//})
//@Import(TestExpenseAnalysisController.MethodSecurityTestConfig.class)
//@ContextConfiguration(classes = { ExpenseAnalysisController.class } )
//class TestExpenseAnalysisController {
//
//    @Configuration
//    @EnableMethodSecurity
//    static class MethodSecurityTestConfig {
//    }
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @MockitoBean
//    TransactionReportUseCase transactionReportUseCase;
//
//    @MockitoBean
//    GeneratePDFDocumentUseCase generatePDFDocumentUseCase;
//
//    @MockitoBean
//    SecurityService securityService;
//
//    @MockitoBean
//    PageMapper pageMapper;
//
//    @MockitoBean(name = "CustomerCanExecuteService")
//    CustomerCanExecuteActionPort customerCanExecuteService;
//
//    private static final Long USER_ID = 1L;
//
//    private static LocalDate start() {
//        return LocalDate.of(2026, 1, 1);
//    }
//
//    private static LocalDate end() {
//        return LocalDate.of(2026, 1, 31);
//    }
//
//    private static <T> PaginatedResponse<T> response(List<T> data) {
//        return new PaginatedResponse<>(
//                data,
//                0,
//                2,
//                10,
//                5,
//                true,
//                false
//        );
//    }
//
//    private PageDataDomain pageDomain(int page, int size) {
//        return new PageDataDomain(page, size, List.of(new PageDataDomain.SortField("createdDate", "DESC")));
//    }
//
//    @Nested
//    @DisplayName("GET /api/v1/expenses/{userId}/summary/category")
//    class Category {
//
//        @Test
//        void shouldReturn200WhenAllowedAndOwner() throws Exception {
//            PageDataDomain pageDataDomain = pageDomain(0, 10);
//            PaginatedResponse<ExpenseByCategory> useCaseResponse = response(List.of());
//
//            given(customerCanExecuteService.isAllowed()).willReturn(true);
//            given(securityService.isOwner(eq(USER_ID), any(Authentication.class))).willReturn(true);
//            given(pageMapper.pageableToPageDataDomain(any())).willReturn(pageDataDomain);
//            given(transactionReportUseCase.totalSummaryByCategoryPaginated(eq(USER_ID), eq(start()), eq(end()), eq(pageDataDomain)))
//                    .willReturn(useCaseResponse);
//
//            mockMvc.perform(
//                            get("/api/v1/expenses/{userId}/summary/category", USER_ID)
//                                    .param("start_date", "2026-01-01")
//                                    .param("end_date", "2026-01-31")
//                                    .param("page", "0")
//                                    .param("size", "10")
//                                    .with(user("user").roles("USER"))
//                    )
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.data").isArray())
//                    .andExpect(jsonPath("$.currentPage").value(0))
//                    .andExpect(jsonPath("$.totalPages").value(2))
//                    .andExpect(jsonPath("$.totalItems").value(10))
//                    .andExpect(jsonPath("$.pageSize").value(5))
//                    .andExpect(jsonPath("$.hasNext").value(true))
//                    .andExpect(jsonPath("$.hasPrevious").value(false));
//
//            then(transactionReportUseCase).should()
//                    .totalSummaryByCategoryPaginated(USER_ID, start(), end(), pageDataDomain);
//        }
//
//        @Test
//        void shouldThrowAccessDeniedWhenNotOwner() {
//            given(customerCanExecuteService.isAllowed()).willReturn(true);
//            given(securityService.isOwner(eq(USER_ID), any(Authentication.class))).willReturn(false);
//
//            assertThatThrownBy(() ->
//                    mockMvc.perform(
//                            get("/api/v1/expenses/{userId}/summary/category", USER_ID)
//                                    .param("start_date", "2026-01-01")
//                                    .param("end_date", "2026-01-31")
//                                    .with(user("user").roles("USER"))
//                    ).andReturn()
//            )
//                    .hasRootCauseInstanceOf(AccessDeniedException.class);
//
//            then(pageMapper).shouldHaveNoInteractions();
//            then(transactionReportUseCase).shouldHaveNoInteractions();
//        }
//    }
//
//    @Nested
//    @DisplayName("GET /api/v1/expenses/{userId}/summary/download")
//    class Download {
//
//        @Test
//        void shouldDownloadPdf() throws Exception {
//            byte[] pdf = new byte[]{1, 2, 3};
//
//            given(customerCanExecuteService.isAllowed()).willReturn(true);
//            given(securityService.isOwner(eq(USER_ID), any(Authentication.class))).willReturn(true);
//            given(generatePDFDocumentUseCase.execute(USER_ID, start(), end())).willReturn(pdf);
//
//            mockMvc.perform(
//                            get("/api/v1/expenses/{userId}/summary/download", USER_ID)
//                                    .param("start_date", "2026-01-01")
//                                    .param("end_date", "2026-01-31")
//                                    .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER"))
//                    )
//                    .andExpect(status().isOk())
//                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-despesas.pdf"))
//                    .andExpect(content().contentType(MediaType.APPLICATION_PDF))
//                    .andExpect(header().longValue(HttpHeaders.CONTENT_LENGTH, pdf.length))
//                    .andExpect(content().bytes(pdf));
//
//            then(generatePDFDocumentUseCase).should().execute(USER_ID, start(), end());
//        }
//    }
//}
package dev.suel.mstransactionapi.infra.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.suel.mstransactionapi.application.exception.AccessDeniedException;
import dev.suel.mstransactionapi.application.gateway.CustomerCanExecuteActionPort;
import dev.suel.mstransactionapi.application.usecase.GeneratePDFDocumentUseCase;
import dev.suel.mstransactionapi.application.usecase.TransactionReportUseCase;
import dev.suel.mstransactionapi.domain.PageDataDomain;
import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
import dev.suel.mstransactionapi.infra.mapper.PageMapper;
import dev.suel.mstransactionapi.infra.services.SecurityService;
import org.assertj.core.api.ThrowableAssert;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ExpenseAnalysisController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = EnableFeignClients.class
        )
)
@TestPropertySource(properties = {
        "services.external.ms-api-check=http://localhost"
})
@Import(TestExpenseAnalysisController.MethodSecurityTestConfig.class)
@ContextConfiguration(classes = {ExpenseAnalysisController.class})
class TestExpenseAnalysisController {

    @Configuration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig {
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TransactionReportUseCase transactionReportUseCase;

    @MockitoBean
    GeneratePDFDocumentUseCase generatePDFDocumentUseCase;

    @MockitoBean
    SecurityService securityService;

    @MockitoBean
    PageMapper pageMapper;

    @MockitoBean(name = "CustomerCanExecuteService")
    CustomerCanExecuteActionPort customerCanExecuteService;

    private static final Long USER_ID = 1L;

    private static LocalDate start() {
        return LocalDate.of(2026, 1, 1);
    }

    private static LocalDate end() {
        return LocalDate.of(2026, 1, 31);
    }

    private static <T> PaginatedResponse<T> response(List<T> data) {
        return new PaginatedResponse<>(
                data,
                0,
                2,
                10,
                5,
                true,
                false
        );
    }

    private PageDataDomain pageDomain(int page, int size) {
        return new PageDataDomain(page, size, List.of(new PageDataDomain.SortField("createdDate", "DESC")));
    }

    private MockHttpServletRequestBuilder auth(MockHttpServletRequestBuilder req) {
        return req.with(user("user").roles("USER"));
    }

    private MockHttpServletRequestBuilder validDates(MockHttpServletRequestBuilder req) {
        return req.param("start_date", "2026-01-01")
                .param("end_date", "2026-01-31");
    }

    private MockHttpServletRequestBuilder paging(MockHttpServletRequestBuilder req) {
        return req.param("page", "0").param("size", "10");
    }

    private void assertThrowsAccessDenied(ThrowableAssert.ThrowingCallable callable) {
        assertThatThrownBy(callable).hasRootCauseInstanceOf(AccessDeniedException.class);
    }

    private void stubHappyPathPaginatedCommon(PageDataDomain pageDataDomain) {
        given(customerCanExecuteService.isAllowed()).willReturn(true);
        given(securityService.isOwner(eq(USER_ID), any(Authentication.class))).willReturn(true);
        given(pageMapper.pageableToPageDataDomain(any(Pageable.class))).willReturn(pageDataDomain);
    }

    private void stubNotOwnerCommon() {
        given(customerCanExecuteService.isAllowed()).willReturn(true);
        given(securityService.isOwner(eq(USER_ID), any(Authentication.class))).willReturn(false);
    }

    private void stubCannotExecuteCommon() {
        given(customerCanExecuteService.isAllowed()).willReturn(false);
    }

    @Nested
    @DisplayName("GET /api/v1/expenses/{userId}/summary/category")
    class Category {

        @Test
        void shouldReturn200WhenAllowedAndOwner() throws Exception {
            PageDataDomain pageDataDomain = pageDomain(0, 10);
            PaginatedResponse<ExpenseByCategory> useCaseResponse = response(List.of());

            stubHappyPathPaginatedCommon(pageDataDomain);
            given(transactionReportUseCase.totalSummaryByCategoryPaginated(eq(USER_ID), eq(start()), eq(end()), eq(pageDataDomain)))
                    .willReturn(useCaseResponse);

            mockMvc.perform(
                            auth(paging(validDates(get("/api/v1/expenses/{userId}/summary/category", USER_ID))))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.currentPage").value(0))
                    .andExpect(jsonPath("$.totalPages").value(2))
                    .andExpect(jsonPath("$.totalItems").value(10))
                    .andExpect(jsonPath("$.pageSize").value(5))
                    .andExpect(jsonPath("$.hasNext").value(true))
                    .andExpect(jsonPath("$.hasPrevious").value(false));

            then(transactionReportUseCase).should()
                    .totalSummaryByCategoryPaginated(USER_ID, start(), end(), pageDataDomain);
        }

        @Test
        void shouldThrowAccessDeniedWhenNotOwner() {
            stubNotOwnerCommon();

            assertThrowsAccessDenied(() -> mockMvc.perform(
                    auth(validDates(get("/api/v1/expenses/{userId}/summary/category", USER_ID)))
            ).andReturn());

            then(pageMapper).shouldHaveNoInteractions();
            then(transactionReportUseCase).shouldHaveNoInteractions();
        }

        @Test
        void shouldReturn401WhenNotAuthenticated() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(
                            paging(validDates(get("/api/v1/expenses/{userId}/summary/category", USER_ID)))
                    )
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldReturn403WhenCustomerCannotExecute() throws Exception {
            stubCannotExecuteCommon();

            mockMvc.perform(
                            auth(validDates(get("/api/v1/expenses/{userId}/summary/category", USER_ID)))
                    )
                    .andExpect(status().isForbidden());

            then(transactionReportUseCase).shouldHaveNoInteractions();
        }

        @Test
        void shouldReturn400WhenInvalidStartDate() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(
                            auth(get("/api/v1/expenses/{userId}/summary/category", USER_ID)
                                    .param("start_date", "invalid")
                                    .param("end_date", "2026-01-31")
                                    .param("page", "0")
                                    .param("size", "10"))
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn400WhenMissingStartDate() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(
                            auth(get("/api/v1/expenses/{userId}/summary/category", USER_ID)
                                    .param("end_date", "2026-01-31")
                                    .param("page", "0")
                                    .param("size", "10"))
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn400WhenMissingEndDate() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(
                            auth(get("/api/v1/expenses/{userId}/summary/category", USER_ID)
                                    .param("start_date", "2026-01-01")
                                    .param("page", "0")
                                    .param("size", "10"))
                    )
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/expenses/{userId}/summary/day")
    class Day {

        @Test
        void shouldReturn200WhenAllowedAndOwner() throws Exception {
            PageDataDomain pageDataDomain = pageDomain(0, 10);
            PaginatedResponse<ExpenseByDay> useCaseResponse = response(List.of());

            stubHappyPathPaginatedCommon(pageDataDomain);
            given(transactionReportUseCase.totalSummaryByDaysPaginated(eq(USER_ID), eq(start()), eq(end()), eq(pageDataDomain)))
                    .willReturn(useCaseResponse);

            mockMvc.perform(
                            auth(paging(validDates(get("/api/v1/expenses/{userId}/summary/day", USER_ID))))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.currentPage").value(0));

            then(transactionReportUseCase).should()
                    .totalSummaryByDaysPaginated(USER_ID, start(), end(), pageDataDomain);
        }

        @Test
        void shouldThrowAccessDeniedWhenNotOwner() {
            stubNotOwnerCommon();

            assertThrowsAccessDenied(() -> mockMvc.perform(
                    auth(validDates(get("/api/v1/expenses/{userId}/summary/day", USER_ID)))
            ).andReturn());

            then(pageMapper).shouldHaveNoInteractions();
            then(transactionReportUseCase).shouldHaveNoInteractions();
        }

        @Test
        void shouldReturn401WhenNotAuthenticated() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(
                            paging(validDates(get("/api/v1/expenses/{userId}/summary/day", USER_ID)))
                    )
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldReturn403WhenCustomerCannotExecute() throws Exception {
            stubCannotExecuteCommon();

            mockMvc.perform(
                            auth(validDates(get("/api/v1/expenses/{userId}/summary/day", USER_ID)))
                    )
                    .andExpect(status().isForbidden());

            then(transactionReportUseCase).shouldHaveNoInteractions();
        }

        @Test
        void shouldReturn400WhenInvalidStartDate() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(
                            auth(get("/api/v1/expenses/{userId}/summary/day", USER_ID)
                                    .param("start_date", "invalid")
                                    .param("end_date", "2026-01-31")
                                    .param("page", "0")
                                    .param("size", "10"))
                    )
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/expenses/{userId}/summary/month")
    class Month {

        @Test
        void shouldReturn200WhenAllowedAndOwner() throws Exception {
            PageDataDomain pageDataDomain = pageDomain(0, 10);
            PaginatedResponse<ExpenseByMonth> useCaseResponse = response(List.of());

            stubHappyPathPaginatedCommon(pageDataDomain);
            given(transactionReportUseCase.totalSummaryByMonthPaginated(eq(USER_ID), eq(start()), eq(end()), eq(pageDataDomain)))
                    .willReturn(useCaseResponse);

            mockMvc.perform(
                            auth(paging(validDates(get("/api/v1/expenses/{userId}/summary/month", USER_ID))))
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.currentPage").value(0));

            then(transactionReportUseCase).should()
                    .totalSummaryByMonthPaginated(USER_ID, start(), end(), pageDataDomain);
        }

        @Test
        void shouldThrowAccessDeniedWhenNotOwner() {
            stubNotOwnerCommon();

            assertThrowsAccessDenied(() -> mockMvc.perform(
                    auth(validDates(get("/api/v1/expenses/{userId}/summary/month", USER_ID)))
            ).andReturn());

            then(pageMapper).shouldHaveNoInteractions();
            then(transactionReportUseCase).shouldHaveNoInteractions();
        }

        @Test
        void shouldReturn401WhenNotAuthenticated() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(
                            paging(validDates(get("/api/v1/expenses/{userId}/summary/month", USER_ID)))
                    )
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldReturn403WhenCustomerCannotExecute() throws Exception {
            stubCannotExecuteCommon();

            mockMvc.perform(
                            auth(validDates(get("/api/v1/expenses/{userId}/summary/month", USER_ID)))
                    )
                    .andExpect(status().isForbidden());

            then(transactionReportUseCase).shouldHaveNoInteractions();
        }

        @Test
        void shouldReturn400WhenInvalidEndDate() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(
                            auth(get("/api/v1/expenses/{userId}/summary/month", USER_ID)
                                    .param("start_date", "2026-01-01")
                                    .param("end_date", "invalid")
                                    .param("page", "0")
                                    .param("size", "10"))
                    )
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/expenses/{userId}/summary/download")
    class Download {

        @Test
        void shouldDownloadPdf() throws Exception {
            byte[] pdf = new byte[]{1, 2, 3};

            given(customerCanExecuteService.isAllowed()).willReturn(true);
            given(securityService.isOwner(eq(USER_ID), any(Authentication.class))).willReturn(true);
            given(generatePDFDocumentUseCase.execute(USER_ID, start(), end())).willReturn(pdf);

            mockMvc.perform(
                            auth(validDates(get("/api/v1/expenses/{userId}/summary/download", USER_ID)))
                    )
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-despesas.pdf"))
                    .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                    .andExpect(header().longValue(HttpHeaders.CONTENT_LENGTH, pdf.length))
                    .andExpect(content().bytes(pdf));

            then(generatePDFDocumentUseCase).should().execute(USER_ID, start(), end());
        }

        @Test
        void shouldThrowAccessDeniedWhenNotOwner() {
            given(customerCanExecuteService.isAllowed()).willReturn(true);
            given(securityService.isOwner(eq(USER_ID), any(Authentication.class))).willReturn(false);

            assertThrowsAccessDenied(() -> mockMvc.perform(
                    auth(validDates(get("/api/v1/expenses/{userId}/summary/download", USER_ID)))
            ).andReturn());

            then(generatePDFDocumentUseCase).shouldHaveNoInteractions();
        }

        @Test
        void shouldReturn401WhenNotAuthenticated() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(
                            validDates(get("/api/v1/expenses/{userId}/summary/download", USER_ID))
                    )
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void shouldReturn403WhenCustomerCannotExecute() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(false);

            mockMvc.perform(
                            auth(validDates(get("/api/v1/expenses/{userId}/summary/download", USER_ID)))
                    )
                    .andExpect(status().isForbidden());

            then(generatePDFDocumentUseCase).shouldHaveNoInteractions();
        }

        @Test
        void shouldReturn400WhenInvalidDate() throws Exception {
            given(customerCanExecuteService.isAllowed()).willReturn(true);

            mockMvc.perform(
                            auth(get("/api/v1/expenses/{userId}/summary/download", USER_ID)
                                    .param("start_date", "invalid")
                                    .param("end_date", "2026-01-31"))
                    )
                    .andExpect(status().isBadRequest());
        }
    }
}
