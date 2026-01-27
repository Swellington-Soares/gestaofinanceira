package dev.suel.msuser.application.usecase.customer;


import dev.suel.msuser.application.gateway.CustomerServicePort;

public class RemoveCustomerByIdUseCase {
    private final CustomerServicePort customerServicePort;

    public RemoveCustomerByIdUseCase(CustomerServicePort customerServicePort) {
        this.customerServicePort = customerServicePort;
    }

   public void execute(Long id) {
        customerServicePort.removeCustomerById(id);
    }
}
