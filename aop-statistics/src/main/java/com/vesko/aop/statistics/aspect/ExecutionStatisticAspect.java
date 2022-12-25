package com.vesko.aop.statistics.aspect;

import com.vesko.aop.statistics.annotation.Statistic;
import com.vesko.aop.statistics.holder.StatisticHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import static com.google.common.base.Strings.isNullOrEmpty;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ExecutionStatisticAspect {
    private final StatisticHolder statisticHolder;

    @Around("@annotation(statistic)")
    public Object executionStatistic(ProceedingJoinPoint joinPoint, Statistic statistic) throws Throwable {
        Object result;
        String region = isNullOrEmpty(statistic.region())
                ? joinPoint.getSignature().getName()
                : statistic.region();
        try {
            result = joinPoint.proceed();
            try {
                statisticHolder.increaseCall(region);
            } catch (Exception ignored) {
            }
            return result;
        } catch (Exception e) {
            if (!statistic.onlySuccessfullyExecuted()) {
                statisticHolder.increaseCall(region);
            }
            throw e;
        }
    }
}
