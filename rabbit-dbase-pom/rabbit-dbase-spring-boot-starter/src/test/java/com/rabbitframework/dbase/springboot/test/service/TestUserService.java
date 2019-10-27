package com.rabbitframework.dbase.springboot.test.service;

import com.rabbitframework.dbase.springboot.test.model.TestUser;

import java.util.List;

public interface TestUserService {
    public List<TestUser> selectTestUser();

    public void insertTestUserRollback();
}
