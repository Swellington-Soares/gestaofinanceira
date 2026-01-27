package dev.suel.msuser.infra.services;

import dev.suel.msuser.application.gateway.CustomerServicePort;
import dev.suel.msuser.application.gateway.PasswordEncoderPort;
import dev.suel.msuser.domain.PageDataDomain;
import dev.suel.msuser.domain.PaginatedResponse;
import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.dto.CustomerInfoResponse;
import dev.suel.msuser.infra.mapper.CustomerMapper;
import dev.suel.msuser.infra.mapper.PageMapper;
import dev.suel.msuser.infra.mapper.PageSortMapper;
import dev.suel.msuser.infra.persistence.entity.CustomerEntity;
import dev.suel.msuser.infra.persistence.repository.CustomerEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class CustomerService implements CustomerServicePort {

    private final CustomerEntityRepository customerEntityRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoderPort passwordEncoderPort;
    private final PageSortMapper pageSortMapper;
    private final PageMapper pageMapper;

    @Override
    @Transactional(readOnly = true)
    public Customer findByCustomerEmail(String email) {
        return customerEntityRepository.findByEmailIgnoreCase(email)
                .map(customerMapper::entityToModel)
                .orElse(null);
    }

    @Override
    @Transactional
    public Customer registerNewCustomer(Customer customer) {
        CustomerEntity customerEntity = CustomerEntity.builder()
                .password(passwordEncoderPort.encode(customer.getPassword()))
                .email(customer.getEmail())
                .name(customer.getName())
                .createdAt(Instant.now())
                .build();
        return customerMapper.entityToModel(customerEntityRepository.save(customerEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findCustomerById(Long id) {
        return customerEntityRepository.findById(id)
                .map(customerMapper::entityToModel)
                .orElse(null);
    }

    @Override
    @Transactional
    public void updateCustomer(Customer customer) {
        customerEntityRepository.findById(customer.getId()).ifPresent(c -> {
            c.setName(customer.getName());
            customerEntityRepository.save(c);
        });
    }

    @Override
    @Transactional
    public boolean changeCustomerPassword(Customer customer, String novaSenha) {
        CustomerEntity customerEntity = customerEntityRepository.findById(customer.getId()).orElse(null);
        if (customerEntity == null) return false;
        if (passwordEncoderPort.matches(novaSenha, customerEntity.getPassword())) return true;
        customerEntity.setPassword(passwordEncoderPort.encode(novaSenha));
        customerEntityRepository.save(customerEntity);
        return true;
    }

    @Override
    public void removeCustomerById(Long id) {
        customerEntityRepository.deleteById(id);
    }


    @Override
    public PaginatedResponse<CustomerInfoResponse> findAll(PageDataDomain pageData) {
        Pageable pageable = PageRequest.of(
                pageData.getPage(),
                pageData.getSize(),
                pageSortMapper.domainToSort(pageData.getSort())
        );
        Page<CustomerInfoResponse> customerPage = customerEntityRepository.findAll(pageable)
                .map(customerMapper::entityToModel)
                .map(customerMapper::modelToResponse);
        return pageMapper.converter(customerPage);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerEntityRepository.existsByEmailIgnoreCase(email);
    }
}
