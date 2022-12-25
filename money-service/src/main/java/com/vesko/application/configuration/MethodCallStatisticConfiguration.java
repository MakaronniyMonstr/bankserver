package com.vesko.application.configuration;

import com.vesko.aop.statistics.StatisticConfiguration;
import com.vesko.aop.statistics.holder.StatisticHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableScheduling
@Import({StatisticConfiguration.class})
@RequiredArgsConstructor
public class MethodCallStatisticConfiguration {
    private final StatisticHolder statisticHolder;

    @Scheduled(fixedRateString = "${statistic.log-delay:1000}")
    public void logStatistic() {
        var statistic = statisticHolder.asMap()
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().getAndSet(0)
                        )
                );
        var overallCalls = statistic.values()
                .stream()
                .reduce(0, Integer::sum);
        if (overallCalls == 0) return;

        StringBuilder result = new StringBuilder("\n");
        statistic
                .forEach((methodName, callCount) -> {
                    if (callCount != 0) {
                        result.append(methodName).append(" called ").append(callCount).append(" times\n");
                    }
                });
        result.append("Overall ").append(overallCalls).append("\n");

        log.info(result.toString());
    }
}
