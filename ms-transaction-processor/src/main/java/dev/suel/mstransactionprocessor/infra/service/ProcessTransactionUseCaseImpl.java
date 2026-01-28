package dev.suel.mstransactionprocessor.infra.service;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionprocessor.application.gateway.BalanceServicePort;
import dev.suel.mstransactionprocessor.application.gateway.ExchangeServicePort;
import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionUseCase;
import dev.suel.mstransactionprocessor.domain.entity.Transaction;
import dev.suel.mstransactionprocessor.infra.kafka.CurrencyQuotationVerifyException;
import dev.suel.mstransactionprocessor.infra.mapper.TransactionMapper;
import dev.suel.mstransactionprocessor.infra.persistence.TransactionEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessTransactionUseCaseImpl implements ProcessTransactionUseCase {

    private final TransactionEntityRepository transactionRepository;
    private final BalanceServicePort balanceServicePort;
    private final ExchangeServicePort exchangeServicePort;
    private final TransactionMapper transactionMapper;

    @Override
    public void execute(TransactionKafkaEventData event) {

        Transaction transaction = transactionRepository
                .findById(event.transactionId())
                .map(transactionMapper::transactionEntityToModel)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        if (!transaction.isPending()) {
            log.info("Transação {} já processada. Status={}",
                    transaction.getTransactionId(),
                    transaction.getStatus());
            return;
        }

        try {
            BigDecimal exchangeRate =
                    exchangeServicePort.getCurrencyExchangeRateToday(
                            transaction.getCurrencyType()
                    );

            transaction.setExchange(exchangeRate);

            BigDecimal balance =
                    balanceServicePort.getBalance(transaction.getUserId());

            switch (transaction.getOperationType()) {
                case DEPOSIT -> {
                    balanceServicePort.updateBalance(
                            transaction.getUserId(),
                            balance.add(transaction.getFinalAmount())
                    );
                    transaction.approve("Depósito realizado com sucesso.");
                }

                case TRANSFER, PURCHASER, WITHDRAW -> {
                    if (balance.compareTo(transaction.getFinalAmount()) >= 0) {
                        balanceServicePort.updateBalance(
                                transaction.getUserId(),
                                balance.subtract(transaction.getFinalAmount())
                        );
                        transaction.approve();
                    } else {

                        transaction.reject("Saldo insuficiente.");
                    }
                }
            }

            transactionRepository.save(
                    transactionMapper.modelToEntity(transaction)
            );

        } catch (CurrencyQuotationVerifyException ex) {
            transaction.reject(ex.getMessage());
            transactionRepository.save(
                    transactionMapper.modelToEntity(transaction)
            );
        } catch (Exception ex) {
            throw ex;
        }
    }

}
