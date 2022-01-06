package ru.taxi.exception;

public class RouteCannotCalculateException extends RuntimeException {
    public RouteCannotCalculateException() {
        super();
    }

    public RouteCannotCalculateException(String message) {
        super(message);
    }

    public RouteCannotCalculateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouteCannotCalculateException(Throwable cause) {
        super(cause);
    }

    protected RouteCannotCalculateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
