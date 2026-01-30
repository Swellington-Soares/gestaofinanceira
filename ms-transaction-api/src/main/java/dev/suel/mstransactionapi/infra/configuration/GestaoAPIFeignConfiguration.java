package dev.suel.mstransactionapi.infra.configuration;


import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class GestaoAPIFeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getCredentials() != null) {
                requestTemplate.header(
                        "Authorization",
                        "Bearer " + auth.getCredentials()
                );
            }
        };
    }

}
