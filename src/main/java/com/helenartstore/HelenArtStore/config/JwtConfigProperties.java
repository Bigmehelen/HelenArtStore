package com.helenartstore.HelenArtStore.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@Data
public class JwtConfigProperties {
    private String secret;
    private long expiration;
}
