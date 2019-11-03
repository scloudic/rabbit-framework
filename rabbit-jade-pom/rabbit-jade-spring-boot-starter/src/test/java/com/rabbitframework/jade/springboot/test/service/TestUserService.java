package com.rabbitframework.jade.springboot.test.service;

import com.rabbitframework.jade.springboot.test.model.TestUser;

import java.util.List;

public interface TestUserService {
    public List<TestUser> selectTestUser();

    public void insertTestUserRollback();
}
