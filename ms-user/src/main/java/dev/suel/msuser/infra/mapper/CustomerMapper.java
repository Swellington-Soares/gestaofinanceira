package dev.suel.msuser.infra.mapper;

import dev.suel.msuser.domain.entity.Customer;
import dev.suel.msuser.domain.entity.Role;
import dev.suel.msuser.domain.valueobject.Email;
import dev.suel.msuser.domain.valueobject.Password;
import dev.suel.msuser.domain.valueobject.PersonName;
import dev.suel.msuser.dto.CustomerInfoResponse;
import dev.suel.msuser.infra.persistence.entity.CustomerEntity;
import dev.suel.msuser.infra.persistence.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomerMapper {


    public Customer entityToModel(CustomerEntity customerEntity) {
        if (customerEntity == null) return null;
        return Customer.builder()
                .password(Password.ofForced(customerEntity.getPassword()))
                .id(customerEntity.getId())
                .name(PersonName.of(customerEntity.getName()))
                .email(Email.of(customerEntity.getEmail()))
                .roles(customerEntity.getRoles().stream()
                        .map(r -> Role.of(r.getName()))
                        .collect(Collectors.toSet()))
                .build();
    }

    public CustomerEntity modelToEntity(Customer customer) {
        if (customer == null) return null;
        return CustomerEntity.builder()
                .id(customer.getId())
                .password(customer.getPassword())
                .name(customer.getName())
                .email(customer.getEmail())
                .createdAt(customer.getCreatedAt())
                .roles(customer.getRoles().stream()
                        .map(r -> RoleEntity.of(r.getName()))
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public CustomerInfoResponse  modelToResponse(Customer customer) {
        if (customer == null) return null;
        return new CustomerInfoResponse(
                customer.getId(),
                customer.getEmail(),
                customer.getName()
        );
    }
}
