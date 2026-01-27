package dev.suel.msuser.application.usecase.customer;


import dev.suel.msuser.application.exception.ResourceAlreadyExistsException;
import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.domain.valueobject.Email;
import dev.suel.msuser.domain.valueobject.Password;
import dev.suel.msuser.domain.valueobject.PersonName;
import dev.suel.msuser.infra.web.dto.CustomerCreateRequest;

public class RegisterNewCustomerUseCase {
    private final CustomerServicePort customerServicePort;

    public RegisterNewCustomerUseCase(CustomerServicePort customerServicePort) {
        this.customerServicePort = customerServicePort;
    }

    public Customer execute(CustomerCreateRequest data) {
        if (customerServicePort.existsByEmail(data.email()))
            throw new ResourceAlreadyExistsException("Já existe um cliente cadastrado com o mesmo e-mail.");

        Password senha = Password.of(data.password());
        Email email = Email.of(data.email());
        PersonName nome = PersonName.of(data.name());

        Customer customer = Customer.builder()
                .email(email)
                .name(nome)
                .password(senha)
                .build();

        return customerServicePort.registerNewCustomer(customer);

    }
}
