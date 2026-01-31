package dev.suel.mstransactionapi.infra.persistence;

import dev.suel.gestaofinanceira.types.TransactionStatus;
import dev.suel.mstransactionapi.dto.ExpenseByCategory;
import dev.suel.mstransactionapi.dto.ExpenseByDay;
import dev.suel.mstransactionapi.dto.ExpenseByMonth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, UUID> {

    @Query("""
            SELECT new dev.suel.mstransactionapi.dto.ExpenseByCategory(
                t.operationType,
                COALESCE(SUM(t.amount * t.exchange), 0)
            )
            FROM Transaction t
            WHERE t.userId = :userId
              AND t.status = 'APPROVED'
              AND t.operationType <> 'DEPOSIT'
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
                COALESCE(SUM(t.amount * t.exchange), 0)
            )
            FROM Transaction t
            WHERE t.userId = :userId
              AND t.status = 'APPROVED'
              AND t.operationType <> 'DEPOSIT'
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
                    COALESCE(SUM(t.amount * t.exchange), 0)
                )
                FROM Transaction t
                WHERE t.userId = :userId
                  AND t.status = 'APPROVED'
                  AND t.operationType <> 'DEPOSIT'
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

    Optional<TransactionEntity> findByIdAndStatus(UUID uuid, TransactionStatus status);

    Optional<TransactionEntity> findByIdAndStatusNot(UUID id, TransactionStatus status);



    @Query(
            value = """
                    SELECT new dev.suel.mstransactionapi.dto.ExpenseByCategory(
                        t.operationType,
                        COALESCE(SUM(t.amount * t.exchange), 0)
                    )
                    FROM Transaction t
                    WHERE t.userId = :userId
                      AND t.status = 'APPROVED'
                      AND t.operationType <> 'DEPOSIT'
                      AND t.createdDate BETWEEN :start AND :end
                    GROUP BY t.operationType
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT t.operationType)
                    FROM Transaction t
                    WHERE t.userId = :userId
                      AND t.status = 'APPROVED'
                      AND t.operationType <> 'DEPOSIT'
                      AND t.createdDate BETWEEN :start AND :end
                    """
    )
    Page<ExpenseByCategory> getSummaryOfTransactionOfUserByCategoryPaginated(Long userId,
                                                                             LocalDateTime startDate,
                                                                             LocalDateTime endDate,
                                                                             Pageable pageable);


    @Query(
            value = """
            SELECT new dev.suel.mstransactionapi.dto.ExpenseByDay(
                t.createdDate,
                COALESCE(SUM(t.amount * t.exchange), 0)
            )
            FROM Transaction t
            WHERE t.userId = :userId
              AND t.status = 'APPROVED'
              AND t.operationType <> 'DEPOSIT'
              AND t.createdDate BETWEEN :start AND :end
            GROUP BY DATE(t.createdDate)
            ORDER BY DATE(t.createdDate)
            """,
            countQuery = """
            SELECT COUNT(DISTINCT DATE(t.createdDate))
            FROM Transaction t
            WHERE t.userId = :userId
              AND t.status = 'APPROVED'
              AND t.operationType <> 'DEPOSIT'
              AND t.createdDate BETWEEN :start AND :end
            """
    )
    Page<ExpenseByDay> getSummaryOfTransactionOfUserByDayPaginated(Long userId,
                                                                        LocalDateTime startDate,
                                                                        LocalDateTime endDate,
                                                                        Pageable pageable
    );


    @Query(
            value = """
            SELECT new dev.suel.mstransactionapi.dto.ExpenseByMonth(
                YEAR(t.createdDate),
                MONTH(t.createdDate),
                COALESCE(SUM(t.amount * t.exchange), 0)
            )
            FROM Transaction t
            WHERE t.userId = :userId
              AND t.status = 'APPROVED'
              AND t.operationType <> 'DEPOSIT'
              AND t.createdDate BETWEEN :start AND :end
            GROUP BY YEAR(t.createdDate), MONTH(t.createdDate)
            ORDER BY YEAR(t.createdDate), MONTH(t.createdDate)
            """,
            countQuery = """
            SELECT COUNT(DISTINCT (YEAR(t.createdDate) * 100 + MONTH(t.createdDate)))
            FROM Transaction t
            WHERE t.userId = :userId
              AND t.status = 'APPROVED'
              AND t.operationType <> 'DEPOSIT'
              AND t.createdDate BETWEEN :start AND :end
            """
    )
    Page<ExpenseByMonth> getSummaryOfTransactionOfUserByMonthPaginated(Long userId,
                                                                          LocalDateTime startDate,
                                                                          LocalDateTime endDate,
                                                                          Pageable pageable);


    default Optional<TransactionEntity> findByIdAndIsPending(UUID id) {
        return findByIdAndStatus(id, TransactionStatus.PENDING);
    }

    default Optional<TransactionEntity> findByIdAndIsNotPending(UUID id) {
        return findByIdAndStatusNot(id, TransactionStatus.PENDING);
    }
}