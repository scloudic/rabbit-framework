package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;

/**
 * 自定义异常抽象类
 *
 * @author: justin
 */
public abstract class RabbitFrameworkException extends RuntimeException {

    public RabbitFrameworkException() {
        super();
    }

    public RabbitFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public RabbitFrameworkException(String message) {
        super(message);
    }

    public RabbitFrameworkException(Throwable cause) {
        super(cause);
    }

    public abstract StatusCode getStatus();
}
