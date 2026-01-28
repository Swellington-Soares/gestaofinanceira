package dev.suel.mstransactionprocessor.infra.external.campioapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrencyExchangeRateInfo(
        String high
) {


}
