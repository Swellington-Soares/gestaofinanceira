package dev.suel.mstransactionprocessor.infra.service;

import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionprocessor.application.exception.InsufficientBalanceForTransactionException;
import dev.suel.mstransactionprocessor.application.gateway.BalanceServicePort;
import dev.suel.mstransactionprocessor.application.gateway.ExchangeServicePort;
import dev.suel.mstransactionprocessor.application.gateway.ProcessTransactionUseCase;
import dev.suel.mstransactionprocessor.application.gateway.exception.TransactionNotFoundException;
import dev.suel.mstransactionprocessor.domain.entity.Transaction;
import dev.suel.mstransactionprocessor.infra.kafka.CurrencyQuotationVerifyException;
import dev.suel.mstransactionprocessor.infra.mapper.TransactionMapper;
import dev.suel.mstransactionprocessor.infra.persistence.TransactionEntityRepository;
import feign.FeignException;
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
        Transaction transaction = null;
        try {
            transaction = transactionRepository.findByIdAndIsPending(event.id())
                    .map(transactionMapper::transactionEntityToModel)
                    .orElseThrow(TransactionNotFoundException::new);


            BigDecimal exchangeRate =
                    exchangeServicePort.getCurrencyExchangeRateToday(
                            transaction.getCurrencyType()
                    );

            transaction.setExchange(exchangeRate);

            BigDecimal balance = balanceServicePort.getBalance(transaction.getUserId());

            switch (transaction.getOperationType()) {
                case DEPOSIT -> {
                    balanceServicePort.updateBalance(
                            transaction.getUserId(),
                            balance.add(transaction.getFinalAmount())
                    );
                    transaction.approve("Depósito realizado com sucesso.");
                }

                case TRANSFER, PURCHASER, WITHDRAW -> {
                    if (transaction.getFinalAmount().compareTo(balance) > 0)
                        throw new InsufficientBalanceForTransactionException();
                    transaction.approve();
                }
                case CREDIT_CARD_PURCHASE -> transaction.approve("Compra no cartão de crédito.");
                case EXTERNAL -> transaction.approve("Movimentação externa.");
            }

            transactionRepository.save(
                    transactionMapper.modelToEntity(transaction)
            );

        } catch (InsufficientBalanceForTransactionException ex) {
            log.info("Transação com ID: {} cancelada.", event.id());
            if (transaction != null) {
                transaction.reject("Saldo insuficiente.");
                transactionRepository.save(
                        transactionMapper.modelToEntity(transaction)
                );
            }
        } catch (TransactionNotFoundException ex) {
            log.info("Transação com ID: {} não encontrada.",  event.id());
        } catch (Exception ex) {
            throw ex;
        }
    }

}
