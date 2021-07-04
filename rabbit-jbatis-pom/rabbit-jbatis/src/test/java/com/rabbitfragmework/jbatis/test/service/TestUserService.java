package com.rabbitfragmework.jbatis.test.service;

import java.util.List;

import com.rabbitfragmework.jbatis.test.model.TestUser;

public interface TestUserService {
	public List<TestUser> selectTestUser();
}
