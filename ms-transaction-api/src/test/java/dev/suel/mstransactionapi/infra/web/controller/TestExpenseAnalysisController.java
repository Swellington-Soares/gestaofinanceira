package dev.suel.mstransactionapi.infra.web.controller;

import dev.suel.mstransactionapi.application.usecase.GeneratePDFDocumentUseCase;
import dev.suel.mstransactionapi.application.usecase.TransactionReportUseCase;
import dev.suel.mstransactionapi.infra.services.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseAnalysisController.class)
class TestExpenseAnalysisController {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionReportUseCase transactionReportUseCase;

    @MockitoBean
    private GeneratePDFDocumentUseCase generatePDFDocumentUseCase;

    @MockitoBean
    private SecurityService securityService;

    @Test
    @WithMockUser
    void shouldReturnTotalByCategoryWhenOwner() throws Exception {
        Long userId = 10L;

        given(securityService.isOwner(eq(userId), any(Authentication.class))).willReturn(true);
        given(transactionReportUseCase.totalByCategory(eq(userId), any(), any())).willReturn(List.of());

        mockMvc.perform(get("/api/v1/expenses/{userId}/summary/category", userId)
                        .param("start_date", "2026-01-01")
                        .param("end_date", "2026-01-10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        then(transactionReportUseCase).should().totalByCategory(eq(userId), any(), any());
    }

    @Test
    @WithMockUser
    void shouldReturnTotalByDayWhenOwner() throws Exception {
        Long userId = 10L;

        given(securityService.isOwner(eq(userId), any(Authentication.class))).willReturn(true);
        given(transactionReportUseCase.totalByDay(eq(userId), any(), any())).willReturn(List.of());

        mockMvc.perform(get("/api/v1/expenses/{userId}/summary/day", userId)
                        .param("start_date", "2026-01-01")
                        .param("end_date", "2026-01-10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        then(transactionReportUseCase).should().totalByDay(eq(userId), any(), any());
    }

    @Test
    @WithMockUser
    void shouldReturnTotalByMonthWhenOwner() throws Exception {
        Long userId = 10L;

        given(securityService.isOwner(eq(userId), any(Authentication.class))).willReturn(true);
        given(transactionReportUseCase.totalByMonth(eq(userId), any(), any())).willReturn(List.of());

        mockMvc.perform(get("/api/v1/expenses/{userId}/summary/month", userId)
                        .param("start_date", "2026-01-01")
                        .param("end_date", "2026-01-10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        then(transactionReportUseCase).should().totalByMonth(eq(userId), any(), any());
    }

    @Test
    @WithMockUser
    void shouldDownloadPdfWhenOwner() throws Exception {
        Long userId = 10L;
        byte[] pdf = new byte[] {1, 2, 3, 4};

        given(securityService.isOwner(eq(userId), any(Authentication.class))).willReturn(true);
        given(generatePDFDocumentUseCase.execute(eq(userId), any(), any())).willReturn(pdf);

        mockMvc.perform(get("/api/v1/expenses/{userId}/summary/download", userId)
                        .param("start_date", "2026-01-01")
                        .param("end_date", "2026-01-10"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=relatorio-transacoes.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(pdf));

        then(generatePDFDocumentUseCase).should().execute(eq(userId), any(), any());
    }

    @Test
    @WithMockUser
    void shouldReturnForbiddenWhenNotOwner() throws Exception {
        Long userId = 10L;

        given(securityService.isOwner(eq(userId), any(Authentication.class))).willReturn(false);

        mockMvc.perform(get("/api/v1/expenses/{userId}/summary/category", userId)
                        .param("start_date", "2026-01-01")
                        .param("end_date", "2026-01-10"))
                .andExpect(status().isForbidden());

        then(transactionReportUseCase).shouldHaveNoInteractions();
        then(generatePDFDocumentUseCase).shouldHaveNoInteractions();
    }
}
