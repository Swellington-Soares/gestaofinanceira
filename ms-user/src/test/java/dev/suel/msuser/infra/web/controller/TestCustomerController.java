package dev.suel.msuser.infra.web.controller;

import dev.suel.msuser.application.usecase.customer.*;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.domain.valueobject.Password;
import dev.suel.msuser.dto.CustomerInfoResponse;
import dev.suel.msuser.dto.FileUploadResponse;
import dev.suel.msuser.infra.mapper.CustomerMapper;
import dev.suel.msuser.infra.mapper.PageMapper;
import dev.suel.msuser.infra.web.dto.CustomerCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TestCustomerController {
    private static final String STRONG_PASSWORD = Password.of("123456!!AAaa").getValue();
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProcessCustomerRegisterByUploadedFileUseCase processCustomerRegisterByUploadedFileUseCase;
    @MockitoBean
    private CheckBatchRegisterStatusUseCase checkBatchRegisterStatusUseCase;
    @MockitoBean
    private ListAllCustomerPaginatedUseCase listAllCustomerPaginatedUseCase;
    @MockitoBean
    private RegisterNewCustomerUseCase registerNewCustomerUseCase;
    @MockitoBean
    private RemoveCustomerByIdUseCase removeCustomerByIdUseCase;
    @MockitoBean
    private UpdateCustomerByIdUseCase updateCustomerByIdUseCase;
    @MockitoBean
    private FindCustomerByIdUseCase findCustomerByIdUseCase;
    @MockitoBean
    private CustomerMapper customerMapper;
    @MockitoBean
    private PageMapper pageMapper;

    @Test
    void deveCriarClienteComSucesso() throws Exception {
        CustomerCreateRequest request =
                new CustomerCreateRequest("joao@email.com", STRONG_PASSWORD, "João Silva");

        Customer customer = Customer.builder().build();
        CustomerInfoResponse response =
                new CustomerInfoResponse(1L, "joao@email.com", "JOÃO SILVA");

        given(registerNewCustomerUseCase.execute(any(CustomerCreateRequest.class)))
                .willReturn(customer);

        given(customerMapper.modelToResponse(customer))
                .willReturn(response);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content("""
                                {
                                  "email": "joao@email.com",
                                  "password": "12345678",
                                  "name": "JOÃO SILVA"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.name").value("JOÃO SILVA"));

        then(registerNewCustomerUseCase).should().execute(any(CustomerCreateRequest.class));
    }

    @Test
    void deveBuscarClientePorId() throws Exception {
        Customer customer = Customer.builder().build();
        CustomerInfoResponse response =
                new CustomerInfoResponse(1L, "João Silva", "joao@email.com");

        given(findCustomerByIdUseCase.execute(1L))
                .willReturn(customer);

        given(customerMapper.modelToResponse(customer))
                .willReturn(response);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        then(findCustomerByIdUseCase).should().execute(1L);
    }


    @Test
    void deveRemoverCliente() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());

        then(removeCustomerByIdUseCase).should().execute(1L);
    }


    @Test
    void deveProcessarUploadDeArquivo() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "clientes.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "conteudo".getBytes()
        );

        FileUploadResponse response =
                new FileUploadResponse("hash123", "Processamento iniciado", Instant.now());

        given(processCustomerRegisterByUploadedFileUseCase.execute(any()))
                .willReturn(response);

        mockMvc.perform(multipart("/api/v1/customers/batch-register")
                        .file(file))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value("hash123"));
    }


}
