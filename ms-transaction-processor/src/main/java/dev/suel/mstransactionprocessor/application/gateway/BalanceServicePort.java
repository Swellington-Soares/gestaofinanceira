package dev.suel.mstransactionprocessor.application.gateway;

import java.math.BigDecimal;

public interface BalanceServicePort {
    void updateBalance(Long userId, BigDecimal amount);
    BigDecimal getBalance(Long userId);
}
