package com.example.foundation.shared.exception;

public class YouVersionIntegrationException extends RuntimeException {

    private final int statusCode;

    public YouVersionIntegrationException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
