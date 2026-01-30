package dev.suel.msuser.application.gateway;

import dev.suel.msuser.domain.PageDataDomain;
import dev.suel.msuser.domain.PaginatedResponse;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.dto.CustomerInfoResponse;


public interface CustomerServicePort {
    Customer findByCustomerEmail(String email);

    Customer registerNewCustomer(Customer customer);

    Customer findCustomerById(Long id);

    void updateCustomer(Customer customer);

    boolean changeCustomerPassword(Customer customer, String novaSenha);

    void removeCustomerById(Long id);

    //FileUploadResponse batchRegisterCustomer(String id, InputStream inputStream) throws IOException;

    //FileUploadStatus checkBatchRegisterStatus(String id);

    PaginatedResponse<CustomerInfoResponse> findAll(PageDataDomain pageData);

    boolean existsByEmail(String email);
}
