package com.scloudic.rabbitfragmework.jbatis.test.service;

import java.util.List;

import com.scloudic.rabbitfragmework.jbatis.test.model.TestUser;

public interface TestUserService {
	public List<TestUser> selectTestUser();
}
