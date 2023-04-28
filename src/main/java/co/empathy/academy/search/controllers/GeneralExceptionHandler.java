package co.empathy.academy.search.controllers;

import co.empathy.academy.search.entities.error.ErrorData;
import co.empathy.academy.search.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;


@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    private final String NOT_FOUND_CODE = "404";
    private final String REPEATED_CODE = "409";
    private final String BAD_REQUEST = "400";

    @ExceptionHandler({FileReadingException.class, IOException.class})
    protected ResponseEntity<Object> handleInvalidFileData(FileReadingException ex, WebRequest wr) {
        ErrorData errorData = new ErrorData(ex.getMessage(), wr.getDescription(false), BAD_REQUEST);
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, wr);
    }

    @ExceptionHandler(NotAvailableDaily.class)
    protected ResponseEntity<Object> handleNotAvailableDaily(NotAvailableDaily ex, WebRequest wr) {
        ErrorData errorData = new ErrorData(ex.getMessage(), wr.getDescription(false), BAD_REQUEST);
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, wr);
    }
}
