package dev.suel.mstransactionprocessor.infra.service;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionRejectUseCase;
import dev.suel.mstransactionprocessor.domain.entity.Transaction;
import dev.suel.mstransactionprocessor.infra.mapper.TransactionMapper;
import dev.suel.mstransactionprocessor.infra.persistence.TransactionEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessTransactionRejectUseCaseImpl implements ProcessTransactionRejectUseCase {

    private final TransactionEntityRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    @Override
    public void execute(TransactionKafkaEventData event, String message) {
        Transaction transaction = transactionRepository
                .findById(event.transactionId())
                .map(transactionMapper::transactionEntityToModel)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        if (!transaction.isPending()) return;

        transaction.reject("Falha técnica após múltiplas tentativas: " + message);

        transactionRepository.save(transactionMapper.modelToEntity(transaction));
        log.error("Transação {} enviada para DLQ",  transaction.getTransactionId());
    }
}
