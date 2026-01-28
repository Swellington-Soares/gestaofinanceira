package dev.suel.mstransactionprocessor.infra.service;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.gestaofinanceira.types.OperationType;
import dev.suel.gestaofinanceira.types.TransactionKafkaEventData;
import dev.suel.mstransactionprocessor.application.gateway.BalanceServicePort;
import dev.suel.mstransactionprocessor.application.gateway.ExchangeServicePort;
import dev.suel.mstransactionprocessor.domain.entity.Transaction;
import dev.suel.mstransactionprocessor.infra.kafka.CurrencyQuotationVerifyException;
import dev.suel.mstransactionprocessor.infra.mapper.TransactionMapper;
import dev.suel.mstransactionprocessor.infra.persistence.TransactionEntity;
import dev.suel.mstransactionprocessor.infra.persistence.TransactionEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class TestProcessTransactionUseCaseImpl {

    private TransactionEntityRepository transactionRepository;
    private BalanceServicePort balanceServicePort;
    private ExchangeServicePort exchangeServicePort;
    private TransactionMapper transactionMapper;

    private ProcessTransactionUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionEntityRepository.class);
        balanceServicePort = mock(BalanceServicePort.class);
        exchangeServicePort = mock(ExchangeServicePort.class);
        transactionMapper = mock(TransactionMapper.class);
        useCase = new ProcessTransactionUseCaseImpl(transactionRepository, balanceServicePort, exchangeServicePort, transactionMapper);
    }

    @Test
    void shouldThrowWhenTransactionNotFound() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);

        given(transactionRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(event));

        then(transactionRepository).should().findById(any());
        verifyNoMoreInteractions(transactionRepository, balanceServicePort, exchangeServicePort, transactionMapper);
    }

    @Test
    void shouldReturnWhenTransactionIsNotPending() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        TransactionEntity entity = mock(TransactionEntity.class);
        Transaction transaction = mock(Transaction.class);

        given(transactionRepository.findById(any())).willReturn(Optional.of(entity));
        given(transactionMapper.transactionEntityToModel(entity)).willReturn(transaction);
        given(transaction.isPending()).willReturn(false);

        useCase.execute(event);

        then(transactionRepository).should().findById(any());
        then(transactionMapper).should().transactionEntityToModel(entity);
        verifyNoMoreInteractions(transactionRepository, balanceServicePort, exchangeServicePort, transactionMapper);
    }

    @Test
    void shouldApproveDepositUpdateBalanceAndSave() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        java.util.UUID transactionId = java.util.UUID.randomUUID();

        TransactionEntity entity = mock(TransactionEntity.class);
        Transaction transaction = mock(Transaction.class);
        TransactionEntity entityToSave = mock(TransactionEntity.class);

        given(event.transactionId()).willReturn(transactionId);
        given(transactionRepository.findById(transactionId)).willReturn(Optional.of(entity));
        given(transactionMapper.transactionEntityToModel(entity)).willReturn(transaction);
        given(transaction.isPending()).willReturn(true);

        given(transaction.getCurrencyType()).willReturn(CurrencyType.USD);
        given(exchangeServicePort.getCurrencyExchangeRateToday(CurrencyType.USD)).willReturn(new BigDecimal("5.25"));

        given(transaction.getUserId()).willReturn(10L);
        given(balanceServicePort.getBalance(10L)).willReturn(new BigDecimal("100.00"));

        given(transaction.getOperationType()).willReturn(OperationType.DEPOSIT);
        given(transaction.getFinalAmount()).willReturn(new BigDecimal("20.00"));

        given(transactionMapper.modelToEntity(transaction)).willReturn(entityToSave);

        useCase.execute(event);

        then(transactionRepository).should().findById(transactionId);
        then(transactionMapper).should().transactionEntityToModel(entity);

        then(exchangeServicePort).should().getCurrencyExchangeRateToday(CurrencyType.USD);
        then(transaction).should().setExchange(new BigDecimal("5.25"));

        then(balanceServicePort).should().getBalance(10L);
        then(balanceServicePort).should().updateBalance(10L, new BigDecimal("120.00"));

        then(transaction).should().approve("Depósito realizado com sucesso.");
        then(transactionMapper).should().modelToEntity(transaction);
        then(transactionRepository).should().save(entityToSave);

        verifyNoMoreInteractions(transactionRepository, balanceServicePort, exchangeServicePort, transactionMapper);
    }


    @Test
    void shouldApproveWhenSufficientBalanceOnTransferAndSave() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        java.util.UUID transactionId = java.util.UUID.randomUUID();

        TransactionEntity entity = mock(TransactionEntity.class);
        Transaction transaction = mock(Transaction.class);
        TransactionEntity entityToSave = mock(TransactionEntity.class);

        given(event.transactionId()).willReturn(transactionId);
        given(transactionRepository.findById(transactionId)).willReturn(Optional.of(entity));
        given(transactionMapper.transactionEntityToModel(entity)).willReturn(transaction);
        given(transaction.isPending()).willReturn(true);

        given(transaction.getCurrencyType()).willReturn(CurrencyType.EUR);
        given(exchangeServicePort.getCurrencyExchangeRateToday(CurrencyType.EUR)).willReturn(new BigDecimal("6.10"));

        given(transaction.getUserId()).willReturn(20L);
        given(balanceServicePort.getBalance(20L)).willReturn(new BigDecimal("50.00"));

        given(transaction.getOperationType()).willReturn(OperationType.TRANSFER);
        given(transaction.getFinalAmount()).willReturn(new BigDecimal("30.00"));

        given(transactionMapper.modelToEntity(transaction)).willReturn(entityToSave);

        useCase.execute(event);

        then(transactionRepository).should().findById(transactionId);
        then(transactionMapper).should().transactionEntityToModel(entity);

        then(exchangeServicePort).should().getCurrencyExchangeRateToday(CurrencyType.EUR);
        then(transaction).should().setExchange(new BigDecimal("6.10"));

        then(balanceServicePort).should().getBalance(20L);
        then(balanceServicePort).should().updateBalance(20L, new BigDecimal("20.00"));

        then(transaction).should().approve();
        then(transactionMapper).should().modelToEntity(transaction);
        then(transactionRepository).should().save(entityToSave);

        verifyNoMoreInteractions(transactionRepository, balanceServicePort, exchangeServicePort, transactionMapper);
    }


    @Test
    void shouldRejectWhenInsufficientBalanceOnWithdrawAndSave() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        java.util.UUID transactionId = java.util.UUID.randomUUID();

        TransactionEntity entity = mock(TransactionEntity.class);
        Transaction transaction = mock(Transaction.class);
        TransactionEntity entityToSave = mock(TransactionEntity.class);

        given(event.transactionId()).willReturn(transactionId);
        given(transactionRepository.findById(transactionId)).willReturn(Optional.of(entity));
        given(transactionMapper.transactionEntityToModel(entity)).willReturn(transaction);
        given(transaction.isPending()).willReturn(true);

        given(transaction.getCurrencyType()).willReturn(CurrencyType.USD);
        given(exchangeServicePort.getCurrencyExchangeRateToday(CurrencyType.USD)).willReturn(new BigDecimal("5.00"));

        given(transaction.getUserId()).willReturn(30L);
        given(balanceServicePort.getBalance(30L)).willReturn(new BigDecimal("10.00"));

        given(transaction.getOperationType()).willReturn(OperationType.WITHDRAW);
        given(transaction.getFinalAmount()).willReturn(new BigDecimal("15.00"));

        given(transactionMapper.modelToEntity(transaction)).willReturn(entityToSave);

        useCase.execute(event);

        then(transactionRepository).should().findById(transactionId);
        then(transactionMapper).should().transactionEntityToModel(entity);

        then(exchangeServicePort).should().getCurrencyExchangeRateToday(CurrencyType.USD);
        then(balanceServicePort).should().getBalance(30L);

        then(transaction).should().setExchange(new BigDecimal("5.00"));
        then(transaction).should().reject("Saldo insuficiente.");

        then(transactionMapper).should().modelToEntity(transaction);
        then(transactionRepository).should().save(entityToSave);

        then(balanceServicePort).shouldHaveNoMoreInteractions();
        then(exchangeServicePort).shouldHaveNoMoreInteractions();
        verifyNoMoreInteractions(transactionRepository, transactionMapper);
    }



    @Test
    void shouldRejectAndSaveWhenCurrencyQuotationFails() {
        TransactionKafkaEventData event = mock(TransactionKafkaEventData.class);
        java.util.UUID transactionId = java.util.UUID.randomUUID();

        TransactionEntity entity = mock(TransactionEntity.class);
        Transaction transaction = mock(Transaction.class);
        TransactionEntity entityToSave = mock(TransactionEntity.class);

        given(event.transactionId()).willReturn(transactionId);
        given(transactionRepository.findById(transactionId)).willReturn(Optional.of(entity));
        given(transactionMapper.transactionEntityToModel(entity)).willReturn(transaction);
        given(transaction.isPending()).willReturn(true);

        given(transaction.getCurrencyType()).willReturn(CurrencyType.USD);
        CurrencyQuotationVerifyException ex = new CurrencyQuotationVerifyException("x");
        given(exchangeServicePort.getCurrencyExchangeRateToday(CurrencyType.USD)).willThrow(ex);

        given(transactionMapper.modelToEntity(transaction)).willReturn(entityToSave);

        useCase.execute(event);

        then(transactionRepository).should().findById(transactionId);
        then(transactionMapper).should().transactionEntityToModel(entity);

        then(exchangeServicePort).should().getCurrencyExchangeRateToday(CurrencyType.USD);
        then(transaction).should().reject("x");
        then(transactionMapper).should().modelToEntity(transaction);
        then(transactionRepository).should().save(entityToSave);

        verifyNoMoreInteractions(transactionRepository, balanceServicePort, exchangeServicePort, transactionMapper);
    }

}
