package com.scloudic.rabbitframework.redisson;

import com.scloudic.rabbitframework.core.exceptions.RabbitFrameworkException;
import com.scloudic.rabbitframework.core.utils.StatusCode;

/**
 * redis缓存出错
 *
 * @author: justin
 */
public class RedisException extends RabbitFrameworkException {
	private static final long serialVersionUID = -5029662342343436456L;

	private StatusCode status = StatusCode.SC_CACHE_ERROR;

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

	@Override
	public StatusCode getStatus() {
		return status;
	}
}
