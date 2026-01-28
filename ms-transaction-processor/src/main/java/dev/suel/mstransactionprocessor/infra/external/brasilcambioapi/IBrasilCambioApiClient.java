package dev.suel.mstransactionprocessor.infra.external.brasilcambioapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "cambio-api",
        url = "${services.external.brasil-cambio-api}"
)
public interface IBrasilCambioApiClient {
    @GetMapping("/moedas")
    List<CurrencyInfo> getCurrencies();

    @GetMapping("/cotacao/{moeda}/{data}")
    CurrencyExchangeRateInfo getCurrency(@PathVariable String moeda, @PathVariable String data);
}
