package edu.lysak.railwaytickets.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(InputValidationException.class)
    public ResponseEntity<Object> handleInputValidationException(
            InputValidationException exception, Locale locale
    ) {
        String errorMessage = messageSource.getMessage(exception.getMessage(), new Object[]{}, locale);
        // TODO - log
        System.out.println(errorMessage);
        return new ResponseEntity<>(
                errorMessage,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<Object> handleBusinessLogicException(
            BusinessLogicException exception, Locale locale
    ) {
        String errorMessage = messageSource.getMessage(exception.getMessage(), new Object[]{}, locale);
        // TODO - log
        System.out.println(errorMessage);
        return new ResponseEntity<>(
                errorMessage,
                HttpStatus.BAD_REQUEST
        );
    }
}
