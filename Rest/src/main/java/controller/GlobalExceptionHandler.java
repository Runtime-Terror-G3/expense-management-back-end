package controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import utils.AuthorizationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> handleConflict(
            RuntimeException ex,
            WebRequest request
    ){
        System.out.println("An exception occurred and it was handled by the global exception handler");
        ex.printStackTrace();

        if(ex instanceof AuthorizationException) {
            AuthorizationException authorizationException = (AuthorizationException) ex;
            HttpStatus statusCode;
            String message;

            switch(authorizationException.code) {
                case FORBIDDEN:
                    message = "Access denied";
                    statusCode = HttpStatus.FORBIDDEN;
                case UNAUTHORIZED:
                    message = "Not logged in";
                    statusCode = HttpStatus.UNAUTHORIZED;
                default:
                    message = "Authorization error";
                    statusCode = HttpStatus.UNAUTHORIZED;
            }

            if(authorizationException.getMessage() != null) {
                message = authorizationException.getMessage();
            }

            handleExceptionInternal(
                    ex,
                    message,
                    new HttpHeaders(),
                    statusCode,
                    request);
        }

        return handleExceptionInternal(
                ex,
                "Internal Server Error",
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
