package dev.suel.msuser.application.usecase.customer;


import dev.suel.msuser.application.exception.ResourceNotFoundException;
import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.application.gateway.PasswordEncoderPort;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.domain.valueobject.Password;
import dev.suel.msuser.domain.valueobject.PersonName;
import dev.suel.msuser.infra.web.dto.CustomerUpdateRequest;

public class UpdateCustomerByIdUseCase {

    private final CustomerServicePort customerServicePort;
    private final PasswordEncoderPort passwordEncoderPort;

    public UpdateCustomerByIdUseCase(CustomerServicePort customerServicePort, PasswordEncoderPort passwordEncoderPort) {
        this.customerServicePort = customerServicePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    public void execute(Long id, CustomerUpdateRequest data) {

        Customer customer = customerServicePort.findCustomerById( id );
        if (customer == null)
            throw new ResourceNotFoundException("Cliente com ID: " + id + " não existe.");

        if (data.name() != null) {
            if (!customer.getName().equalsIgnoreCase(data.name())){
                PersonName personName = PersonName.of(data.name());
                customer.setName( personName );
                customerServicePort.updateCustomer(customer);
            }
        }

        if (data.password() != null && data.newPassword() != null) {

            if (!passwordEncoderPort.matches(data.password(), customer.getPassword()))
                throw new IllegalArgumentException("A password atual incorreta. Não é possível atualizar.");

            Password newPassword = Password.of(data.newPassword());

            if (!customerServicePort.changeCustomerPassword(customer, newPassword.getValue()))
                throw new IllegalArgumentException("Não possível trocar sua senha, verifique as informações.");
        }

    }

}
