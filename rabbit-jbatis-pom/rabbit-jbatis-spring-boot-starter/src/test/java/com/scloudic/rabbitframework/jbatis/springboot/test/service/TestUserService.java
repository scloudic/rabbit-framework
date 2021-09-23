package com.scloudic.rabbitframework.jbatis.springboot.test.service;

import com.scloudic.rabbitframework.jbatis.service.IService;
import com.scloudic.rabbitframework.jbatis.springboot.test.model.TestUser;

public interface TestUserService extends IService<TestUser> {
    public void createTable();
}
