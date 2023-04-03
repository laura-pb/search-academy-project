package co.empathy.academy.search.controllers;

import co.empathy.academy.search.entities.error.ErrorData;
import co.empathy.academy.search.exceptions.FileReadingException;
import co.empathy.academy.search.exceptions.InvalidJsonFileException;
import co.empathy.academy.search.exceptions.UserAlreadyExistsException;
import co.empathy.academy.search.exceptions.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    private final String NOT_FOUND_CODE = "404";
    private final String REPEATED_CODE = "409";
    private final String BAD_REQUEST = "400";

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex, WebRequest wr) {
        ErrorData errorData = new ErrorData(ex.getMessage(), wr.getDescription(false), NOT_FOUND_CODE);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorData);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException ex, WebRequest wr) {
        ErrorData errorData = new ErrorData(ex.getMessage(), wr.getDescription(false), REPEATED_CODE);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorData);
    }

    @ExceptionHandler(FileReadingException.class)
    protected ResponseEntity<Object> handleInvalidFileData(FileReadingException ex, WebRequest wr) {
        ErrorData errorData = new ErrorData(ex.getMessage(), wr.getDescription(false), BAD_REQUEST);
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, wr);
    }
}
