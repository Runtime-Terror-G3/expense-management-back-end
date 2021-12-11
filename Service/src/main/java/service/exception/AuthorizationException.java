package utils;

public class AuthorizationException extends RuntimeException{
    public Constants.AuthorizationExceptionCode code;

    public AuthorizationException(Constants.AuthorizationExceptionCode code) {
        this.code = code;
    }

    public AuthorizationException(String message, Constants.AuthorizationExceptionCode code) {
        super(message);
        this.code = code;
    }
}
