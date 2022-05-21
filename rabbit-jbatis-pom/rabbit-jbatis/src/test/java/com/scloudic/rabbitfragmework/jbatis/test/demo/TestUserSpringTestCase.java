package com.scloudic.rabbitfragmework.jbatis.test.demo;

import java.util.List;

import com.scloudic.rabbitfragmework.jbatis.test.service.TestUserService;
import com.scloudic.rabbitfragmework.jbatis.test.model.TestUser;
import org.junit.Test;

import com.scloudic.rabbitfragmework.jbatis.test.core.AbstractSpringTestCase;

public class TestUserSpringTestCase extends AbstractSpringTestCase {
	@Test
	public void testSelectAll() {
		TestUserService testUserService = getBean(TestUserService.class);
		List<TestUser> testUsers = testUserService.selectTestUser();
		for (TestUser testUser : testUsers) {
			System.out.println("id:" + testUser.getId() + ",name:"
					+ testUser.getTestName());

		}
	}
}
