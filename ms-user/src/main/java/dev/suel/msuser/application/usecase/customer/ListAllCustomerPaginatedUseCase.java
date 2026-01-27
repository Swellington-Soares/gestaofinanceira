package dev.suel.msuser.application.usecase.customer;

import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.domain.PageDataDomain;
import dev.suel.msuser.domain.PaginatedResponse;
import dev.suel.msuser.dto.CustomerInfoResponse;

public class ListAllCustomerPaginatedUseCase {

    private final CustomerServicePort customerServicePort;

    public ListAllCustomerPaginatedUseCase(CustomerServicePort customerServicePort) {
        this.customerServicePort = customerServicePort;
    }

    public PaginatedResponse<CustomerInfoResponse> execute(PageDataDomain pageDataDomain){
        return customerServicePort.findAll(pageDataDomain);
    }
}
