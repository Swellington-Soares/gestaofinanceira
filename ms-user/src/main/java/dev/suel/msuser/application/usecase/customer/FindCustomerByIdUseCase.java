package dev.suel.msuser.application.usecase.customer;


import dev.suel.msuser.application.exception.ResourceNotFoundException;
import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.domain.entity.Customer;

public class FindCustomerByIdUseCase {

    private final CustomerServicePort customerServicePort;

    public FindCustomerByIdUseCase(CustomerServicePort customerServicePort) {
        this.customerServicePort = customerServicePort;
    }

    public Customer execute(Long id) {
        Customer customer = customerServicePort.findCustomerById(id);
        if (customer == null)
            throw new ResourceNotFoundException("Cliente com ID: " + id + " não encontrado.");
        return customer;
    }
}
