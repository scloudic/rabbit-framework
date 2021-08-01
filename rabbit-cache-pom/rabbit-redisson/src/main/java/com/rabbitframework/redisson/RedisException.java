package com.rabbitframework.redisson;

/**
 * redis缓存出错
 *
 * @author: justin
 * @date: 2017-08-01 上午12:54
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