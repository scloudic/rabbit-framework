package com.rabbitframework.dbase.exceptions;

public class DbaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DbaseException() {
		super();
	}

	public DbaseException(String message) {
		super(message);
	}

	public DbaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public DbaseException(Throwable cause) {
		super(cause);
	}
}
