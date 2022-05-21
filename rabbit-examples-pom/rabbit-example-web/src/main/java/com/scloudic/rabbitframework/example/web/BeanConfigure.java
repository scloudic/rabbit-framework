package com.scloudic.rabbitframework.example.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigure {
	@Bean
	@ConditionalOnMissingBean
	public TestBean testBean() {
		TestBean testBean = new TestBean();
		return testBean;
	}
}
