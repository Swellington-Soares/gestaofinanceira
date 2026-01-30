package dev.suel.mstransactionapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "dev.suel.mstransactionapi.infra.external")
public class MsTransactionApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsTransactionApiApplication.class, args);
    }

}
