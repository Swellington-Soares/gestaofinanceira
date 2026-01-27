package dev.suel.msuser.infra.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jwt")
@Getter
@Setter
public class TokenConfigProperty {
    private String issuer = "SW_ISSUER";
    private String signKey = "5656442345";
    private int accessTokenValidityMinutes = 20;
    private int refreshTokenValidityMinutes = 60;
}
