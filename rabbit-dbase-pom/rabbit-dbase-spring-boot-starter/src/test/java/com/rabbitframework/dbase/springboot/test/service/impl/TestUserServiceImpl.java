package com.rabbitframework.dbase.springboot.test.service.impl;

import com.rabbitframework.dbase.springboot.test.mapper.TestUserMapper;
import com.rabbitframework.dbase.springboot.test.model.TestUser;
import com.rabbitframework.dbase.springboot.test.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TestUserServiceImpl implements TestUserService {
    @Autowired
    private TestUserMapper testUserMapper;

    @Override
    public List<TestUser> selectTestUser() {
        return testUserMapper.selectTestUser();
    }

    @Transactional
    @Override
    public void insertTestUserRollback() {
        TestUser testUser = new TestUser();
        testUser.setAge(new Date());
        testUser.setTestName("hao");
        testUser.setId(2);
        testUserMapper.insertByEntity(testUser);
        TestUser testUser2 = new TestUser();
        testUser2.setAge(new Date());
        testUser2.setTestName("hao");
        testUser2.setId(3);
        testUserMapper.insertByEntity(testUser2);
        throw new RuntimeException("test");
    }
}
