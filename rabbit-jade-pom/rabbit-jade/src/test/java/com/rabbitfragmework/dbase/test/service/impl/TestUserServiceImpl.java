package com.rabbitfragmework.dbase.test.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.rabbitfragmework.dbase.test.mapper.TestUserMapper;
import com.rabbitfragmework.dbase.test.model.TestUser;
import com.rabbitfragmework.dbase.test.service.TestUserService;
import org.springframework.stereotype.Service;

@Service("testUserService")
public class TestUserServiceImpl implements TestUserService {
	@Resource
	private TestUserMapper testUserMapper;

	@Override
	public List<TestUser> selectTestUser() {
		return testUserMapper.selectTestUser();
	}

}
