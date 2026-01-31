package dev.suel.mstransactionapi.infra.configuration.jackson;


import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class JacksonConfiguration {


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            SimpleModule bigDecimalModule = new SimpleModule();
            bigDecimalModule.addSerializer(BigDecimal.class, new BigDecimalSerializer());
            builder.modules(bigDecimalModule);

            builder.modules(
                    bigDecimalModule,
                    new JavaTimeModule()
            );

            builder.featuresToDisable(
                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
            );
        };
    }
}
