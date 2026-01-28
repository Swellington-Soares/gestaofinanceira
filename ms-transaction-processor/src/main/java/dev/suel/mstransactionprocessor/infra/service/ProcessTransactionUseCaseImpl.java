package dev.suel.mstransactionprocessor.infra.service;

import dev.suel.mstransactionprocessor.application.gateway.BalanceServicePort;
import dev.suel.mstransactionprocessor.application.gateway.ExchangeServicePort;
import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionUseCase;
import dev.suel.mstransactionprocessor.domain.TransactionStatus;
import dev.suel.mstransactionprocessor.domain.entity.Transaction;
import dev.suel.mstransactionprocessor.dto.TransactionKafkaEventData;
import dev.suel.mstransactionprocessor.infra.mapper.TransactionMapper;
import dev.suel.mstransactionprocessor.infra.persistence.TransactionEntity;
import dev.suel.mstransactionprocessor.infra.persistence.TransactionEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProcessTransactionUseCaseImpl implements ProcessTransactionUseCase {

    private static final Logger log =
            LoggerFactory.getLogger(ProcessTransactionUseCaseImpl.class);

    private final TransactionEntityRepository transactionRepository;
    private final BalanceServicePort balanceServicePort;
    private final ExchangeServicePort exchangeServicePort;
    private final TransactionMapper transactionMapper;

    @Override
    public void execute(TransactionKafkaEventData event) {
        Transaction transaction = transactionRepository
                .findById(event.transactionId())
                .map(transactionMapper::transactionEntityToModel)
                .orElseThrow(() -> new IllegalArgumentException("Transaction não encontrada"));

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            log.info("Transação {} já processada. Status={}",
                    transaction.getTransactionId(),
                    transaction.getStatus());
            return;
        }

        BigDecimal exchangeRateToday = exchangeServicePort.getCurrencyExchangeRateToday(transaction.getCurrencyType());
        transaction.setExchange(exchangeRateToday);

        BigDecimal currentBalance = balanceServicePort.getBalance(transaction.getUserId());

        switch (transaction.getOperationType()) {
            case DEPOSIT ->  {
                balanceServicePort.updateBalance(transaction.getUserId(), currentBalance.add(transaction.getFinalAmount()));
                transaction.approve();
            }
            case TRANSFER,
                 PURCHASER,
                 WITHDRAW -> {
                if (currentBalance.compareTo(transaction.getFinalAmount()) > 0) {
                    balanceServicePort.updateBalance(
                            transaction.getUserId(),
                            currentBalance.subtract(transaction.getFinalAmount())
                    );
                    transaction.approve();
                } else {
                    transaction.reject();
                }
            }
        }

        transactionRepository.save(
                transactionMapper.modelToEntity(transaction)
        );
    }
}
