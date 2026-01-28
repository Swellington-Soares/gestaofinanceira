package dev.suel.mstransactionprocessor.infra.service;

import dev.suel.mstransactionprocessor.application.gateway.ExchangeServicePort;
import dev.suel.mstransactionprocessor.domain.BusinessDayCalculator;
import dev.suel.mstransactionprocessor.domain.CurrencyType;
import dev.suel.mstransactionprocessor.infra.external.brasilcambioapi.CurrencyExchangeRate;
import dev.suel.mstransactionprocessor.infra.external.brasilcambioapi.CurrencyExchangeRateInfo;
import dev.suel.mstransactionprocessor.infra.external.brasilcambioapi.IBrasilCambioApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeServicePort {

    private final IBrasilCambioApiClient brasilCambioApiClient;

    private float getSellQuoteToday(CurrencyType currencyCode) {

        LocalDate currentDate =
                !BusinessDayCalculator.isWeekend(LocalDate.now()) ? LocalDate.now() :
                        BusinessDayCalculator.previousBusinessDay(LocalDate.now());
        CurrencyExchangeRateInfo info = brasilCambioApiClient.getCurrency(currencyCode.name(), currentDate.toString());
        List<CurrencyExchangeRate> rates = info.exchanges();
        return rates.
                stream()
                .skip(rates.size() - 1)
                .findFirst()
                .map(CurrencyExchangeRate::sellQuote)
                .orElse(1.0f);

    }

    @Override
    public BigDecimal getCurrencyExchangeRateToday(CurrencyType currencyType) {
        if (currencyType.equals(CurrencyType.BRL)) return new BigDecimal("1.0");
        return new BigDecimal(getSellQuoteToday(currencyType));
    }
}
