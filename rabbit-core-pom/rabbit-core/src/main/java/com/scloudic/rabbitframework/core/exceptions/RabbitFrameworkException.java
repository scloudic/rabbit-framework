package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;

/**
 * 自定义异常抽象类
 *
 * @author: justin
 */
public abstract class RabbitFrameworkException extends RuntimeException {
    private String description;

    public RabbitFrameworkException() {
        super();
        this.description = getStatus().getMsg();
    }

    public RabbitFrameworkException(String message, Throwable cause) {
        super(message, cause);
        this.description = message;
    }

    public RabbitFrameworkException(String message) {
        super(message);
        this.description = message;
    }

    public RabbitFrameworkException(Throwable cause) {
        super(cause);
        this.description = getStatus().getMsg();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract StatusCode getStatus();
}
