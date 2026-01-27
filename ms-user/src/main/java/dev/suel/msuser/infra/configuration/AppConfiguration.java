package dev.suel.msuser.infra.configuration;


import dev.suel.msuser.application.usecase.customer.*;
import dev.suel.msuser.application.usecase.login.LoginUserCase;
import dev.suel.msuser.application.usecase.login.RefreshTokenUserCase;
import dev.suel.msuser.infra.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

    private final CustomerService customerService;
    private final PasswordEncodeService passwordEncoderService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationService authenticateService;
    private final JwtTokenService jwtTokenService;
    private final BatchRegisterCustomerService batchRegisterCustomerService;

    @Bean
    public UpdateCustomerByIdUseCase updateCustomerByIdUseCase() {
        return new UpdateCustomerByIdUseCase(customerService, passwordEncoderService);
    }

    @Bean
    public FindCustomerByIdUseCase findCustomerByIdUseCase() {
        return new FindCustomerByIdUseCase(customerService);
    }

    @Bean
    public RegisterNewCustomerUseCase registerNewCustomerUseCase(){
        return new RegisterNewCustomerUseCase(customerService);
    }

    @Bean
    public RemoveCustomerByIdUseCase removeCustomerByIdUseCase(){
        return new RemoveCustomerByIdUseCase(customerService);
    }

    @Bean
    public RegisterNewCustomerByFileUseCase registerNewsCustomersByFile() {
        return new RegisterNewCustomerByFileUseCase(batchRegisterCustomerService);
    }


    @Bean
    public CheckBatchRegisterStatusUseCase checkBatchCustomerRegisterStatus() {
        return new CheckBatchRegisterStatusUseCase(batchRegisterCustomerService);
    }

    @Bean
    public ListAllCustomerPaginatedUseCase listAllCustomerPaginatedUseCase() {
        return new ListAllCustomerPaginatedUseCase(customerService);
    }

    @Bean
    public RefreshTokenUserCase refreshTokenUserCase() {
        return new RefreshTokenUserCase(jwtTokenService, refreshTokenService, customerService);
    }

    @Bean
    public LoginUserCase loginUserCase() {
        return new LoginUserCase(authenticateService, jwtTokenService, refreshTokenService, customerService);
    }

    @Bean
    public ProcessCustomerRegisterByUploadedFileUseCase processCustomerRegisterByUploadedFileUseCase() {
        return new ProcessCustomerRegisterByUploadedFileUseCase(batchRegisterCustomerService);
    }
}
