package dev.suel.mstransactionprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "dev.suel.mstransactionprocessor.infra.external")
public class MsTransactionProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsTransactionProcessorApplication.class, args);
    }

}
