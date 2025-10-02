package cc.aliko.payment_service.exception;

public class TokenFetchException extends RuntimeException {
    public TokenFetchException(String message) {
        super(message);
    }
    public TokenFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
