package dev.suel.mstransactionprocessor.infra.external.brasilcambioapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CurrencyExchangeRateInfo(
        @JsonProperty("cotacoes")
        List<CurrencyExchangeRate> exchanges,

        @JsonProperty("moeda")
        String currencyCode,

        @JsonProperty("data")
        String date
) {


}
