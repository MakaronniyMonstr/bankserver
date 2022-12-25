package com.vesko.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Objects.requireNonNull;

public class CollectionUtil {
    public static <T> T getRandomElement(List<T> list) {
        requireNonNull(list);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List can't be empty");
        }

        var index = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(index);
    }
}
