package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private final HttpStatus status;
    private final String message;
    private final String reason;
    private final List<String> errors;
    private final LocalDateTime timestamp = LocalDateTime.now();
}