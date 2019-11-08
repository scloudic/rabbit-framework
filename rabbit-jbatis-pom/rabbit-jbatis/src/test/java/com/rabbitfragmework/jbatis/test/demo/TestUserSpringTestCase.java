package com.rabbitfragmework.jbatis.test.demo;

import java.util.List;

import com.rabbitfragmework.jbatis.test.service.TestUserService;
import org.junit.Test;

import com.rabbitfragmework.jbatis.test.core.AbstractSpringTestCase;
import com.rabbitfragmework.jbatis.test.model.TestUser;

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
