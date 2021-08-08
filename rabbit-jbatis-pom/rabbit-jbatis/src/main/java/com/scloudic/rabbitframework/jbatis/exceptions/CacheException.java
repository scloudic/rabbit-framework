package com.scloudic.rabbitframework.jbatis.exceptions;

public class CacheException extends DbaseException {
	private static final long serialVersionUID = 1L;

	public CacheException() {
		super();
	}

	public CacheException(String msg) {
		super(msg);
	}

	public CacheException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public CacheException(Throwable throwable) {
		super(throwable);
	}
}
