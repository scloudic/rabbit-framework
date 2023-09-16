package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.core.utils.StringUtils;

/**
 * 自定义异常抽象类
 *
 * @author: justin
 */
public abstract class RabbitFrameworkException extends RuntimeException {
    private String description;

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

    public String getDescription() {
        if (StringUtils.isBlank(description)) {
            description = getStatus().getMsg();
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassName() {
        return getClass().getName();
    }

    public abstract StatusCode getStatus();
}
