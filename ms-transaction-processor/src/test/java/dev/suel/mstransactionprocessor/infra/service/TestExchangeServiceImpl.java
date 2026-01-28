package dev.suel.mstransactionprocessor.infra.service;

import dev.suel.gestaofinanceira.types.CurrencyType;
import dev.suel.mstransactionprocessor.infra.external.campioapi.CurrencyExchangeRateInfo;
import dev.suel.mstransactionprocessor.infra.external.campioapi.ICambioApiClient;
import dev.suel.mstransactionprocessor.infra.kafka.CurrencyQuotationVerifyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class TestExchangeServiceImpl {

    private ICambioApiClient cambioApiClient;
    private ExchangeServiceImpl service;

    @BeforeEach
    void setUp() {
        cambioApiClient = mock(ICambioApiClient.class);
        service = new ExchangeServiceImpl(cambioApiClient);
    }

    @Test
    void shouldReturnOneWhenCurrencyIsBRL() {
        BigDecimal result = service.getCurrencyExchangeRateToday(CurrencyType.BRL);

        assertEquals(new BigDecimal("1.0"), result);
        verifyNoMoreInteractions(cambioApiClient);
    }

    @Test
    void shouldReturnExchangeRateWhenCurrencyIsNotBRL() {
        CurrencyType currency = CurrencyType.USD;
        CurrencyExchangeRateInfo info = mock(CurrencyExchangeRateInfo.class);

        given(info.high()).willReturn("5.25");
        given(cambioApiClient.getCurrency(currency.name()))
                .willReturn(Map.of("USD", info));

        BigDecimal result = service.getCurrencyExchangeRateToday(currency);

        assertEquals(new BigDecimal("5.25"), result);
        then(cambioApiClient).should().getCurrency(currency.name());
        then(info).should().high();
        verifyNoMoreInteractions(cambioApiClient, info);
    }


    @Test
    void shouldThrowExceptionWhenNoQuotationInfoFound() {
        CurrencyType currency = CurrencyType.EUR;

        given(cambioApiClient.getCurrency(currency.name()))
                .willReturn(Map.of());

        assertThrows(CurrencyQuotationVerifyException.class,
                () -> service.getCurrencyExchangeRateToday(currency));

        then(cambioApiClient).should().getCurrency(currency.name());
        verifyNoMoreInteractions(cambioApiClient);
    }
}
