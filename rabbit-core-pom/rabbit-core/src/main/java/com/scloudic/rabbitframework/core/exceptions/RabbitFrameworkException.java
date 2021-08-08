package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;

/**
 * 自定义异常抽象类
 *
 * @author: justin
 * @date: 2017-07-31 下午10:31
 */
public abstract class RabbitFrameworkException extends RuntimeException {
    protected String description;

    public RabbitFrameworkException() {
        super();
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
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public abstract StatusCode getStatus();
}
