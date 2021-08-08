package com.scloudic.rabbitframework.generator.exceptions;

public class GeneratorException extends RuntimeException {
	private static final long serialVersionUID = -3343243408198768193L;

	public GeneratorException() {
		super();
	}

	public GeneratorException(String message) {
		super(message);
	}

	public GeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneratorException(Throwable cause) {
		super(cause);
	}

}
