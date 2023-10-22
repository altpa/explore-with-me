package ru.practicum.exception;

public class EventTimeException extends RuntimeException {
    public EventTimeException() {
        super();
    }

    public EventTimeException(String message) {
        super(message);
    }

    public EventTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
