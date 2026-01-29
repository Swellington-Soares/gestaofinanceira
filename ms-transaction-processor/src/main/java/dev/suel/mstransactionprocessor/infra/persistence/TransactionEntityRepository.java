package dev.suel.mstransactionprocessor.infra.persistence;


import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, UUID> {


    Optional<TransactionEntity> findByIdAndStatus(UUID uuid, TransactionStatus status);
    Optional<TransactionEntity> findByIdAndStatusNot(UUID id, TransactionStatus status);

    default Optional<TransactionEntity> findByIdAndIsPending(UUID id) {
        return findByIdAndStatus(id, TransactionStatus.PENDING);
    }

    default Optional<TransactionEntity> findByIdAndIsNotPending(UUID id) {
        return findByIdAndStatusNot(id, TransactionStatus.PENDING);
    }

}