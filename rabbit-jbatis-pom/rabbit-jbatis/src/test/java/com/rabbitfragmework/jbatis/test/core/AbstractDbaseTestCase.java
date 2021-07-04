package com.rabbitfragmework.jbatis.test.core;

import java.io.IOException;

import org.junit.Before;

public abstract class AbstractDbaseTestCase {
	protected abstract void initDbase() throws IOException;

	@Before
	public void before() {
		try {
			initDbase();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
