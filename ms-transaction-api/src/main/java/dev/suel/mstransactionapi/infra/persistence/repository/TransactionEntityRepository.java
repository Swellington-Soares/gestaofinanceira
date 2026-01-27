package dev.suel.mstransactionapi.infra.persistence.repository;

import dev.suel.mstransactionapi.domain.PaginatedResponse;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
import dev.suel.mstransactionapi.infra.persistence.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, UUID> {

    @Query("""
            SELECT new dev.suel.mstransactionapi.dto.ExpenseByCategory(
                t.operationType,
                COALESCE(SUM(t.amount), 0)
            )
            FROM Transaction t
            WHERE t.userId = :userId
              AND t.status = 'APPROVED'
              AND t.operationType IN ('WITHDRAW', 'TRANSFER', 'PURCHASER')
              AND t.createdDate BETWEEN :start AND :end
            GROUP BY t.operationType
            """)
    List<ExpenseByCategory> totalByCategory(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );


    @Query("""
            SELECT new dev.suel.mstransactionapi.dto.ExpenseByDay(
                t.createdDate,
                COALESCE(SUM(t.amount), 0)
            )
            FROM Transaction t
            WHERE t.userId = :userId
              AND t.status = 'APPROVED'
              AND t.operationType IN ('WITHDRAW', 'TRANSFER', 'PURCHASER')
              AND t.createdDate BETWEEN :start AND :end
            GROUP BY DATE(t.createdDate)
            ORDER BY DATE(t.createdDate)
            """)
    List<ExpenseByDay> totalByDay(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
                SELECT new dev.suel.mstransactionapi.dto.ExpenseByMonth(
                    YEAR(t.createdDate),
                    MONTH(t.createdDate),
                    COALESCE(SUM(t.amount), 0)
                )
                FROM Transaction t
                WHERE t.userId = :userId
                  AND t.status = 'APPROVED'
                  AND t.operationType IN ('WITHDRAW', 'TRANSFER', 'PURCHASER')
                  AND t.createdDate BETWEEN :start AND :end
                GROUP BY YEAR(t.createdDate), MONTH(t.createdDate)
                ORDER BY YEAR(t.createdDate), MONTH(t.createdDate)
            """)
    List<ExpenseByMonth> totalByMonth(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );


    Page<TransactionEntity> findAllByUserId(Long userId, Pageable pageable);
}