package com.mobylab.springbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class RequestException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public RequestException(HttpStatusCode status, String message) {
        super(message);
        this.status = HttpStatus.resolve(status.value());
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
