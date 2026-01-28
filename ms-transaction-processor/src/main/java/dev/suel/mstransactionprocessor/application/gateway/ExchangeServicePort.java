package dev.suel.mstransactionprocessor.application.gateway;

import dev.suel.mstransactionprocessor.domain.CurrencyType;

import java.math.BigDecimal;

public interface ExchangeServicePort {
    BigDecimal getCurrencyExchangeRateToday(CurrencyType currencyType);
}
