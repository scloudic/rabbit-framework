package com.scloudic.rabbitframework.jbatis.springboot.test.service;

import com.scloudic.rabbitframework.jbatis.springboot.test.model.TestUser;

import java.util.List;

public interface TestUserService {
    public List<TestUser> selectTestUser();

    public void insertTestUserRollback();
}
