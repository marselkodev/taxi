package ru.taxi.exception;

public class OperationWithXmlException extends RuntimeException {
    public OperationWithXmlException() {
        super();
    }

    public OperationWithXmlException(String message) {
        super(message);
    }

    public OperationWithXmlException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationWithXmlException(Throwable cause) {
        super(cause);
    }

    protected OperationWithXmlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
