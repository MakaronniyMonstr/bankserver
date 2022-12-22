package com.vesko.application.exception;

import java.util.Collections;
import java.util.Map;

public class JsonParsableException extends RuntimeException {
    public static final String ERROR = "error";

    public JsonParsableException(String message) {
        super(message);
    }

    public Map<String, String> getJsonSerializableMessage() {
        return Collections.singletonMap(ERROR, getMessage());
    }
}
