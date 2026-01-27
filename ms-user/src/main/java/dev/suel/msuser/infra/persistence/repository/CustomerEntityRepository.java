package dev.suel.msuser.infra.persistence.repository;

import dev.suel.msuser.infra.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerEntityRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}