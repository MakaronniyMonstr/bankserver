package com.vesko.aop.statistics;

import com.vesko.aop.statistics.holder.DefaultStatisticHolder;
import com.vesko.aop.statistics.holder.StatisticHolder;
import com.vesko.aop.statistics.aspect.ExecutionStatisticAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatisticConfiguration {
    @Bean
    public ExecutionStatisticAspect executionStatisticAspect(StatisticHolder statisticHolder) {
        return new ExecutionStatisticAspect(statisticHolder);
    }

    @Bean
    public StatisticHolder statisticHolder() {
        return new DefaultStatisticHolder();
    }
}
