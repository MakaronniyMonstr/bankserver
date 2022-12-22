package com.vesko.application.exception;

import static java.lang.String.format;
public class ResourceNotFoundException extends JsonParsableException {
    public ResourceNotFoundException(Object id) {
        super(format("Resource with id '%s' not found", id));
    }
}
