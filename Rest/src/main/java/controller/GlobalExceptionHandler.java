package controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import service.exception.AuthorizationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> handleConflict(
            RuntimeException ex,
            WebRequest request
    ){
        System.out.println("An exception occurred and it was handled by the global exception handler");
        ex.printStackTrace();
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Internal server error";

        if(ex instanceof AuthorizationException) {
            AuthorizationException authorizationException = (AuthorizationException) ex;

            switch(authorizationException.code) {
                case FORBIDDEN:
                    message = "Access denied";
                    statusCode = HttpStatus.FORBIDDEN;
                    break;
                case UNAUTHORIZED:
                    message = "Not logged in";
                    statusCode = HttpStatus.UNAUTHORIZED;
                    break;
                default:
                    message = "Authorization error";
                    statusCode = HttpStatus.UNAUTHORIZED;
            }

            if(authorizationException.getMessage() != null) {
                message = authorizationException.getMessage();
            }
        }

        return handleExceptionInternal(
                ex,
                message,
                new HttpHeaders(),
                statusCode,
                request);
    }
}
