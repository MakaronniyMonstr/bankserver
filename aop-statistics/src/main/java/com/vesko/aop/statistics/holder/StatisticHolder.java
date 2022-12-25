package com.vesko.aop.statistics.holder;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface StatisticHolder {
    void increaseCall(String name);

    Map<String, AtomicInteger> asMap();
}
