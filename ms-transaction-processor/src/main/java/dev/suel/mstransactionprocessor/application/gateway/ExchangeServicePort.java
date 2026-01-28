package dev.suel.mstransactionprocessor.application.gateway;

import dev.suel.gestaofinanceira.types.CurrencyType;

import java.math.BigDecimal;

public interface ExchangeServicePort {
    BigDecimal getCurrencyExchangeRateToday(CurrencyType currencyType);
}
