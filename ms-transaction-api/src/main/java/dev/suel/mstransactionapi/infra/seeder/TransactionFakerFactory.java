package dev.suel.mstransactionapi.infra.seeder;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionStatus;
import dev.suel.mstransactionapi.domain.entity.Transaction;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

public class TransactionFakerFactory {
    private static final Faker faker =
            new Faker(Locale.of("pt", "BR"), new Random(Instant.now().getEpochSecond()));

    private TransactionFakerFactory() {
    }

    public static Transaction randomStatus(Long userId) {
        return Transaction.withId()
                .createdDate(randomPastDate())
                .processedDate(randomProcessedDate())
                .amount(randomAmount())
                .exchange(randomExchange())
                .operationType(randomOperationType())
                .status(randomStatus())
                .message(faker.lorem().sentence())
                .currencyType(randomCurrency())
                .userId( userId != null ? userId : faker.number().numberBetween(1L, 10L))
                .build();
    }

    public static Transaction approved(Long userId) {
        return Transaction.withId()
                .createdDate(randomPastDate())
                .processedDate(randomProcessedDate())
                .amount(randomAmount())
                .exchange(randomExchange())
                .operationType(randomOperationType())
                .status(TransactionStatus.APPROVED)
                .message(faker.lorem().sentence())
                .currencyType(randomCurrency())
                .userId( userId != null ? userId : faker.number().numberBetween(1L, 10L))
                .build();
    }

    public static Transaction rejected(Long userId) {
        return Transaction.withId()
                .createdDate(randomPastDate())
                .processedDate(randomProcessedDate())
                .amount(randomAmount())
                .exchange(randomExchange())
                .operationType(randomOperationType())
                .status(TransactionStatus.REJECTED)
                .message(faker.lorem().sentence())
                .currencyType(randomCurrency())
                .userId( userId != null ? userId : faker.number().numberBetween(1L, 10L))
                .build();
    }

    public static Transaction pending(Long userId) {
        return Transaction.withId()
                .createdDate(LocalDateTime.now())
                .amount(randomAmount())
                .exchange(BigDecimal.ONE)
                .operationType(randomOperationType())
                .status(TransactionStatus.PENDING)
                .currencyType(randomCurrency())
                .userId( userId != null ? userId : faker.number().numberBetween(1L, 10L))
                .build();
    }

    private static LocalDateTime randomPastDate() {
        return faker.timeAndDate()
                .past(30, java.util.concurrent.TimeUnit.DAYS)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private static LocalDateTime randomProcessedDate() {
        return faker.bool().bool()
                ? LocalDateTime.now()
                : null;
    }

    private static BigDecimal randomAmount() {
        return BigDecimal.valueOf(
                faker.number().randomDouble(2, 10, 10_000)
        ).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal randomExchange() {
        return BigDecimal.valueOf(
                faker.number().randomDouble(4, 0, 10)
        ).setScale(4, RoundingMode.HALF_UP);
    }

    private static OperationType randomOperationType() {
        OperationType[] values = OperationType.values();
        return values[faker.random().nextInt(values.length)];
    }

    private static TransactionStatus randomStatus() {
        TransactionStatus[] values = TransactionStatus.values();
        return values[faker.random().nextInt(values.length)];
    }

    private static CurrencyType randomCurrency() {
        CurrencyType[] values = CurrencyType.values();
        return values[faker.random().nextInt(values.length)];
    }
}
