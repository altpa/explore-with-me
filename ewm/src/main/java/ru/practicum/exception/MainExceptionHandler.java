package ru.practicum.exception;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EventTimeException.class})
    public ResponseEntity<ApiError> handleEventTimeException(Exception ex) throws IOException {
        ApiError apiError = new ApiError(
                CONFLICT,
                "Time not valid",
                ex.getLocalizedMessage(),
                Collections.singletonList(error(ex))
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<ApiError> handleConflictException(Exception ex) throws IOException {
        ApiError apiError = new ApiError(
                CONFLICT,
                "Conflict in request",
                ex.getLocalizedMessage(),
                Collections.singletonList(error(ex))
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiError> handleBadRequest(Exception ex) throws IOException {
        ApiError apiError = new ApiError(
                BAD_REQUEST,
                "Value not valid",
                ex.getLocalizedMessage(),
                Collections.singletonList(error(ex))
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ApiError> handleEwmBadRequest(BadRequestException ex) throws IOException {
        ApiError apiError = new ApiError(
                BAD_REQUEST,
                "Error in request",
                ex.getLocalizedMessage(),
                Collections.singletonList(error(ex))
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ObjectNotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(ObjectNotFoundException ex) throws IOException {
        ApiError apiError = new ApiError(
                NOT_FOUND,
                "Not found in DB",
                ex.getLocalizedMessage(),
                Collections.singletonList(error(ex))
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @Override
    public @NonNull ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                        @NonNull HttpHeaders headers,
                                                                        @NonNull HttpStatus status,
                                                                        @NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add("error in " + error.getField() + " with value = " + error.getRejectedValue());
        }

        ApiError apiError = new ApiError(
                BAD_REQUEST,
                "Данные в реквесте невалидны",
                ex.getLocalizedMessage(),
                errors
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
		List<String> errors = new ArrayList<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
		}

		ApiError apiError = new ApiError(
                BAD_REQUEST,
                "Данные в реквесте невалидны",
				ex.getLocalizedMessage(),
                errors
        );

		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        ApiError apiError = null;
        try {
            apiError = new ApiError(
                    BAD_REQUEST,
                    "No request param",
                    ex.getLocalizedMessage(),
                    Collections.singletonList(error(ex))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    private String error(Exception e) throws IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String error = sw.toString();
        sw.close();
        pw.close();
        return error;
    }
}
