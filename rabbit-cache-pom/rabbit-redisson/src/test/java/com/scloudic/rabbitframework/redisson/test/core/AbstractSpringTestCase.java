package com.scloudic.rabbitframework.redisson.test.core;

import org.junit.After;
import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:applicationContext*.xml" })
public abstract class AbstractSpringTestCase extends AbstractJUnit4SpringContextTests {
	@Before
	public void setUp() {
		
	}

	@After
	public void tearDown() throws Exception {
	}

}
