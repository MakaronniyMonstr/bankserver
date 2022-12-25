package com.vesko.application.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "cache"
)
@Getter
public class CacheProperties {
    private Integer cacheSize = 100;
}
