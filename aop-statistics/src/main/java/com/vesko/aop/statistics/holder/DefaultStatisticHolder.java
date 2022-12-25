package com.vesko.aop.statistics.holder;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DefaultStatisticHolder implements StatisticHolder {
    private final LoadingCache<String, AtomicInteger> methodsCallCount = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<>() {
                        @Override
                        public AtomicInteger load(String s) {
                            return new AtomicInteger(0);
                        }
                    }
            );

    @Override
    public void increaseCall(String methodName) {
        try {
            methodsCallCount.get(methodName).incrementAndGet();
        } catch (ExecutionException e) {
            log.error("Can't increase method call", e);
        }
    }

    @Override
    public Map<String, AtomicInteger> asMap() {
        return methodsCallCount.asMap();
    }
}
