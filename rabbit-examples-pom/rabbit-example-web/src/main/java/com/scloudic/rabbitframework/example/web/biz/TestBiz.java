package com.scloudic.rabbitframework.example.web.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scloudic.rabbitframework.example.web.TestBean;

@Component
public class TestBiz {
	@Autowired
	private TestBean testBean;

	public String test(String name) {
		return testBean.test(name);
	}

	public void setTestBean(TestBean testBean) {
		this.testBean = testBean;
	}
}
