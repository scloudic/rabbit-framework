package com.scloudic.rabbitframework.redisson;

import com.scloudic.rabbitframework.core.exceptions.RabbitFrameworkException;
import com.scloudic.rabbitframework.core.utils.StatusCode;

/**
 * redis缓存出错
 *
 * @author: justin
 */
public class RedissonLockLockException extends RabbitFrameworkException {
	private static final long serialVersionUID = -5029662342343436456L;
	private StatusCode status = StatusCode.REDIS_LOCK_ERROR;

	public RedissonLockLockException() {
		super();
	}

	public RedissonLockLockException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedissonLockLockException(String message) {
		super(message);
	}

	public RedissonLockLockException(Throwable cause) {
		super(cause);
	}

	@Override
	public StatusCode getStatus() {
		return status;
	}
}
