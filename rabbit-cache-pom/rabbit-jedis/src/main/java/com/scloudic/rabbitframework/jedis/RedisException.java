package com.scloudic.rabbitframework.jedis;

/**
 * redis缓存出错
 *
 * @author: justin
 */
public class RedisException extends RuntimeException {
    private static final long serialVersionUID = -5029662342343436456L;

    public RedisException() {
        super();
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisException(String message) {
        super(message);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }
}
