package dev.suel.mstransactionprocessor.infra.external.campioapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(
        name = "cambio-api",
        url = "${services.external.cambio-api}"
)
public interface ICambioApiClient {

    @GetMapping("/{moeda}-BRL")
    Map<String, CurrencyExchangeRateInfo> getCurrency(@PathVariable String moeda);
}
