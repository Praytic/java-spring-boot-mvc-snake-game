package com.vchernogorov;

public class SnakeCreationException extends RuntimeException {

    public SnakeCreationException() {
    }

    public SnakeCreationException(String message) {
        super(message);
    }

    public SnakeCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnakeCreationException(Throwable cause) {
        super(cause);
    }

    public SnakeCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
