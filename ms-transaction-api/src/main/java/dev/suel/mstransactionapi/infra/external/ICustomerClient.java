package dev.suel.mstransactionapi.infra.external;


import dev.suel.mstransactionapi.infra.configuration.GestaoAPIFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "ms-api",
        url = "${services.external.ms-api-check}",
        configuration = GestaoAPIFeignConfiguration.class
)
public interface ICustomerClient {
    @GetMapping
    ResponseEntity<Void> checkCustomer();
}
