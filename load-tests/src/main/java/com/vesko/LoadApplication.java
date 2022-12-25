package com.vesko;

import com.vesko.configuration.Properties;
import com.vesko.configuration.PropertiesReader;
import com.vesko.service.BalanceService;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static com.vesko.util.CollectionUtil.getRandomElement;
import static java.util.Collections.nCopies;

@Slf4j
public class LoadApplication {
    public static final String CONFIGURATION_FILE = "conf.yml";
    private static final AtomicInteger updateCounter = new AtomicInteger(0);
    private static final AtomicInteger failedUpdateCounter = new AtomicInteger(0);

    public static void main(String[] args) throws IOException, InterruptedException {
        var properties = PropertiesReader.loadProperties(CONFIGURATION_FILE);
        properties.validate();
        var retrofit = new Retrofit.Builder()
                .baseUrl(properties.getHost())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        var balanceService = retrofit.create(BalanceService.class);
        log.info("Successfully created balanceService instance");
        var task = createTask(properties, balanceService);
        runLoadTuskInMultiThread(properties, task);
    }

    private static void runLoadTuskInMultiThread(Properties properties, Callable<Void> task) throws InterruptedException {
        var threadCount = properties.getThreadCount();

        try (var executor = Executors.newFixedThreadPool(threadCount)) {
            executor.invokeAll(nCopies(threadCount, task));
            log.info("Successful updates: {}", updateCounter.get());
            log.info("Unsuccessful updates: {}", failedUpdateCounter.get());
        }
    }

    private static Callable<Void> createTask(Properties properties, BalanceService balanceService) {
        var readProbability = (double) properties.getReadQuota() /
                (properties.getReadQuota() + properties.getWriteQuota());
        return () -> {
            while (true) {
                try {
                    long start = System.currentTimeMillis();
                    if (ThreadLocalRandom.current().nextDouble() < readProbability) {
                        var response = balanceService.getBalanceById(
                                getRandomElement(properties.getReadIdList())
                        ).execute();
                        assert response.body() != null;
                        log.info("Time elapsed {} for read query. Code: {}. Balance: {}", System.currentTimeMillis() - start, response.code(), response.body().getRubles());
                    } else {
                        var response = balanceService.updateBalanceById(
                                getRandomElement(properties.getWriteIdList()),
                                1
                        ).execute();
                        if (response.code() == 200) {
                            updateCounter.incrementAndGet();
                        } else {
                            failedUpdateCounter.incrementAndGet();
                        }
                        log.info("Time elapsed {} for update query. Code: {}", System.currentTimeMillis() - start, response.code());
                    }

                } catch (IOException e) {
                    log.error("Something went wrong with executor pool", e);
                    throw new RuntimeException(e);
                }
            }
        };
    }
}