package com.example.springboot3.exception;

import com.example.springboot3.payload.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // WHEN ANY VALIDATION FAILS.
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        HashMap<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    //CUSTOM ERROR HANDLING USE THERE WHERE WE WANT TO RETURN NOT FOUND
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

    }


    @ExceptionHandler(InvalidOrderOperationException.class)
    public ResponseEntity<ErrorDetails> handleInvalidOrderOperation(InvalidOrderOperationException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    //    FOR WHEN WE SEND MALFORMED JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleInvalidJson(HttpMessageNotReadableException ex, WebRequest request) {
        String message = "Malformed JSON request";
        if (ex.getCause() != null) {
            message += ": " + ex.getCause().getMessage();
        }
        ErrorDetails errorDetails = new ErrorDetails(new Date(), message, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    //WHEN WRONG URL IS VISITED
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNotFoundUrl(NoHandlerFoundException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "No handler found for URL: " + ex.getRequestURL(), request.getRequestURI());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // Handles database unique constraint violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String message;
        Throwable root = ex.getRootCause();
        if (root != null) {
            String rootMessage = root.getMessage().toLowerCase();
            if (rootMessage.contains("duplicate") || rootMessage.contains("unique")) {
                message = "Duplicate record error: A record with the same unique field already exists.";
            } else {
                message = "Database error: " + root.getMessage();
            }
        } else {
            message = "Database error: " + ex.getMessage();
        }
        ErrorDetails errorDetails = new ErrorDetails(new java.util.Date(), message, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Handle HTTP method not allowed errors
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDetails> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        String message = "HTTP method " + ex.getMethod() + " is not supported for this endpoint.";
        ErrorDetails errorDetails = new ErrorDetails(new Date(), message, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Handle missing request parameters
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDetails> handleMissingParams(MissingServletRequestParameterException ex, WebRequest request) {
        String message = "Missing required request parameter: " + ex.getParameterName();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), message, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Handle type mismatches (e.g., passing string instead of number)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetails> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = "Parameter '" + ex.getName() + "' should be of type " + Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), message, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // FOR ANY OTHER KIND OF EXCEPTION
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}