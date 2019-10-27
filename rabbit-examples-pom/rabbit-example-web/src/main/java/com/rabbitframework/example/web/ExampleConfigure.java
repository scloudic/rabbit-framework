package com.rabbitframework.example.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.rabbitframework.example.web.biz.TestBiz;

@Configuration
public class ExampleConfigure {
	@Bean
	@ConditionalOnMissingBean
	@DependsOn("testBean")
	public TestBiz testBiz(TestBean testBean) {
		TestBiz testBiz = new TestBiz();
		testBiz.setTestBean(testBean);
		return testBiz;
	}
}