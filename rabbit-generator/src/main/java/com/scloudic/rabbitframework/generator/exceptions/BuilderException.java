package com.scloudic.rabbitframework.generator.exceptions;

public class BuilderException extends GeneratorException {
	private static final long serialVersionUID = -8589551402753291377L;

	public BuilderException() {
		super();
	}

	public BuilderException(String message) {
		super(message);
	}

	public BuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	public BuilderException(Throwable cause) {
		super(cause);
	}
}
