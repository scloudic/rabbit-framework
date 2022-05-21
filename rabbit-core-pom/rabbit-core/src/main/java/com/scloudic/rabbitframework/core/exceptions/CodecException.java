package com.scloudic.rabbitframework.core.exceptions;

import com.scloudic.rabbitframework.core.utils.StatusCode;

/**
 * @author justin.liang
 */
public class CodecException extends RabbitFrameworkException {
    private StatusCode status = StatusCode.CODEC;
    
    public CodecException() {
        super();
    }

    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodecException(String message) {
        super(message);
    }

    public CodecException(Throwable cause) {
        super(cause);
    }

    @Override
    public StatusCode getStatus() {
        return status;
    }
}
