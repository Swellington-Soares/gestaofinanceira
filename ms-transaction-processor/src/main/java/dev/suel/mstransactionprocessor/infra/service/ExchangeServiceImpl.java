package dev.suel.mstransactionprocessor.infra.service;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.mstransactionprocessor.application.gateway.ExchangeServicePort;
import dev.suel.mstransactionprocessor.infra.external.campioapi.CurrencyExchangeRateInfo;
import dev.suel.mstransactionprocessor.infra.external.campioapi.ICambioApiClient;
import dev.suel.mstransactionprocessor.infra.kafka.CurrencyQuotationVerifyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeServicePort {

    private final ICambioApiClient cambioClient;

    private float getSellQuoteToday(CurrencyType currencyCode) {
        Map<String, CurrencyExchangeRateInfo> info = cambioClient.getCurrency(currencyCode.name());
        CurrencyExchangeRateInfo value = info.values().stream().findFirst().orElseThrow(
                () -> new CurrencyQuotationVerifyException("Não foi possível obter informação sobre a cotação.")
        );
        return Float.parseFloat(value.high());

    }

    @Override
    public BigDecimal getCurrencyExchangeRateToday(CurrencyType currencyType) {
        if (currencyType.equals(CurrencyType.BRL)) return new BigDecimal("1.0");
        return new BigDecimal(getSellQuoteToday(currencyType));
    }
}
