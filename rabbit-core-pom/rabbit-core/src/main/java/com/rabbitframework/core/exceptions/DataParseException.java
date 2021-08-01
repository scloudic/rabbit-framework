package com.rabbitframework.core.exceptions;

public class DataParseException  extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataParseException() {
        super();
    }

    public DataParseException(String message) {
        super(message);
    }

    public DataParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataParseException(Throwable cause) {
        super(cause);
    }
}
