package com.rabbitfragmework.jbatis.test.core;

import org.junit.After;
import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:applicationContext*.xml" })
public abstract class AbstractSpringTestCase extends
		AbstractJUnit4SpringContextTests {
	@Before
	public void setUp() {

	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> bean) {
		String classNameStr = bean.getSimpleName();
		String classNameFirstChar = classNameStr.charAt(0) + "";
		String beanName = classNameFirstChar.toLowerCase()
				+ classNameStr.substring(1);
		return (T) applicationContext.getBean(beanName);
	}

}
