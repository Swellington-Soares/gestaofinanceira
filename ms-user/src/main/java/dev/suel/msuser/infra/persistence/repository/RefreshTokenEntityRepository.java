package dev.suel.msuser.infra.persistence.repository;

import dev.suel.msuser.infra.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenEntityRepository extends JpaRepository<RefreshTokenEntity, Long> {
    void deleteByEmail(String email);
    Optional<RefreshTokenEntity> findByToken(String token);
}