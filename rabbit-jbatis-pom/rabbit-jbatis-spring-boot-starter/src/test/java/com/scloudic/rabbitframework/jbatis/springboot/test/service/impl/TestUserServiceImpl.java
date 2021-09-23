package com.scloudic.rabbitframework.jbatis.springboot.test.service.impl;

import com.scloudic.rabbitframework.jbatis.service.IServiceImpl;
import com.scloudic.rabbitframework.jbatis.springboot.test.mapper.TestUserMapper;
import com.scloudic.rabbitframework.jbatis.springboot.test.model.TestUser;
import com.scloudic.rabbitframework.jbatis.springboot.test.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestUserServiceImpl extends IServiceImpl<TestUserMapper, TestUser> implements TestUserService {
    @Autowired
    private TestUserMapper testUserMapper;

    @Override
    public void createTable() {
        testUserMapper.createTestUser();
    }

    @Override
    public TestUserMapper getBaseMapper() {
        return testUserMapper;
    }

//    @Override
//    public List<TestUser> selectTestUser() {
//        return testUserMapper.selectTestUser();
//    }

//
//    @Transactional
//    @Override
//    public void insertTestUserRollback() {
//        TestUser testUser = new TestUser();
//        testUser.setAge(new Date());
//        testUser.setTestName("hao");
//        testUser.setId(6);
//        testUserMapper.insertByEntity(testUser);
//        TestUser testUser2 = new TestUser();
//        testUser2.setAge(new Date());
//        testUser2.setTestName("hao");
//        testUser2.setId(7);
//        testUserMapper.insertByEntity(testUser2);
//        throw new RuntimeException("test");
//    }
//
//    @Override
//    public List<TestUser> selectDynamic() {
//        DynamicSQLBuilder dynamicSQLBuilder = new DynamicSQLBuilder("select t.* from test_user t");
//        dynamicSQLBuilder.join(Join.INNER, "test_user tu", "t.id=tu.id");
//        Where where = new Where();
//        where.setDynamicSQL(dynamicSQLBuilder.generateSQL());
//        return null;
////        return testUserMapper.selectDynamic(where, TestUser.class);
//    }
//
//    @Override
//    public Long countDynamic() {
//        return null;
//    }
}
