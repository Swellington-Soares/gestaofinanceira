package dev.suel.mstransactionprocessor.infra.service;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionRejectUseCase;
import dev.suel.mstransactionprocessor.application.gateway.exception.TransactionNotFoundException;
import dev.suel.mstransactionprocessor.domain.entity.Transaction;
import dev.suel.mstransactionprocessor.infra.mapper.TransactionMapper;
import dev.suel.mstransactionprocessor.infra.persistence.TransactionEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessTransactionRejectUseCaseImpl implements ProcessTransactionRejectUseCase {

    private final TransactionEntityRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public void execute(TransactionKafkaEventData event, String message) {

        try {
            Transaction transaction = transactionRepository
                    .findByIdAndIsPending(event.id())
                    .map(transactionMapper::transactionEntityToModel)
                    .orElseThrow(TransactionNotFoundException::new);

            transaction.reject(
                    message == null ?
                            "Falha técnica após múltiplas tentativas." :
                            "Falha técnica após múltiplas tentativas: " + message);

            transactionRepository.save(transactionMapper.modelToEntity(transaction));
            log.error("Transação {} enviada para DLQ", transaction.getId());
        } catch (TransactionNotFoundException ex) {
            log.info("Transação com ID: {} não encontrada.", event.id());
        } catch (Exception e) {
             log.info("Problema Grave: {}", e.getMessage());
        }
    }
}
