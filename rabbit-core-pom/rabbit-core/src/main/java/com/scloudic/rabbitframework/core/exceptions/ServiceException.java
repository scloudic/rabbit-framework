package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;

public class ServiceException extends RabbitFrameworkException {
    private static final long serialVersionUID = 8714902911973669718L;
    private StatusCode status = StatusCode.SC_INTERNAL_SERVER_ERROR;

    public ServiceException() {
        super();
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    @Override
    public StatusCode getStatus() {
        return status;
    }
}
