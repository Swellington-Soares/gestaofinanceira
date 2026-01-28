package dev.suel.mstransactionapi.infra.seeder;


import dev.suel.mstransactionapi.domain.entity.Transaction;
import dev.suel.mstransactionapi.infra.mapper.TransactionMapper;
import dev.suel.mstransactionapi.infra.persistence.TransactionEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class DatabaseSeederRunnable implements ApplicationRunner {

    private final TransactionEntityRepository transactionRepository;
    private final TransactionMapper mapper;

    @Override
    public void run(ApplicationArguments args) {
        try {
            if (transactionRepository.count() == 0) {
                var transactions = new ArrayList<Transaction>();
                for (int i = 0; i < 15; i++) {
                    transactions.add(TransactionFakerFactory.approved(null));
                    transactions.add(TransactionFakerFactory.pending(null));
                    transactions.add(TransactionFakerFactory.rejected(null));
                }
                transactionRepository.saveAll(
                        transactions.stream().map(mapper::modelToEntity).toList()
                );
            }
        } catch (Exception ignored) {}
    }
}
