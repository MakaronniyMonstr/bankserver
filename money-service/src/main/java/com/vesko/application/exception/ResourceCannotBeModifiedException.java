package com.vesko.application.exception;

public class ResourceCannotBeModifiedException extends JsonParsableException {
    public ResourceCannotBeModifiedException(String message) {
        super(message);
    }
}
