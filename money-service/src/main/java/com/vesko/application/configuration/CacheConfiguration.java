package com.vesko.application.configuration;

import com.google.common.cache.CacheBuilder;
import com.vesko.application.property.CacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching
@RequiredArgsConstructor
public class CacheConfiguration {
    private final CacheProperties properties;

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .maximumSize(properties.getCacheSize())
                                .expireAfterWrite(10, TimeUnit.SECONDS)
                                .build()
                                .asMap(),
                        false
                );
            }
        };
    }
}
