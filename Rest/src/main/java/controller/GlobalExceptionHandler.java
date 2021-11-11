package controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleConflict(
            Exception ex,
            WebRequest request
    ){
        System.out.println("Am exception occurred and it was handled by the global exception handler");
        ex.printStackTrace();
        return handleExceptionInternal(
                ex,
                "Internal Server Error",
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
