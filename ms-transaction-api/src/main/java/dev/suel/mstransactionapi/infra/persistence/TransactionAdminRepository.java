package dev.suel.mstransactionapi.infra.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Este repositório é usado apenas para o propósito de desenvolvimento, não o exponha em produção.
 */

@Profile("dev")
@Repository
public interface TransactionAdminRepository extends JpaRepository<TransactionEntity, UUID> {
}
