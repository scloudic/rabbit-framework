package com.rabbitfragmework.dbase.test.demo;

import com.rabbitfragmework.dbase.test.core.DataAccessTestCase;
import com.rabbitfragmework.dbase.test.mapper.TestUserMapper;
import com.rabbitfragmework.dbase.test.model.TestUser;
import com.rabbitframework.jade.mapping.RowBounds;
import com.rabbitframework.jade.mapping.param.Criteria;
import com.rabbitframework.jade.mapping.param.WhereParamType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestUserTestCase extends DataAccessTestCase {
    private static final Logger logger = LoggerFactory.getLogger(TestUserTestCase.class);

    @Test
    public void createTestUser() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        int result = testMapper.createTestUser();
        logger.info("result:" + result);
    }

    @Test
    public void insertTestUser() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        TestUser testUser = new TestUser();
        testUser.setTestName("testAuto");
        int result = testMapper.insertByEntity(testUser);
        System.out.println(result);
        System.out.println(testUser.getId());
    }

    @Test
    public void batchInsertTestUser() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        TestUser testUser = new TestUser();
        testUser.setTestName("batchInsertTestUser");
        TestUser testUser1 = new TestUser();
        testUser1.setTestName("batchInsertTestUser1");
        List<TestUser> testUsers = new ArrayList<TestUser>();
        testUsers.add(testUser);
        testUsers.add(testUser1);
        int result = testMapper.bacthInsert(testUsers);
        System.out.println(result);
    }


    @Test
    public void updateTestUserById() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        int result = testMapper.updateTest(14L, "updateName");
        System.out.println("result:" + result);
    }

    @Test
    public void updateTestByUser() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        TestUser testUser = new TestUser();
        testUser.setId(13L);
        testUser.setTestName("testAutoupdate");
        testMapper.updateByEntity(testUser);
    }

    @Test
    public void selectTestUserAll() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        List<TestUser> testUsers = testMapper.selectTestUser();
        for (TestUser testUser : testUsers) {
            System.out.println("id:" + testUser.getId() + ",name:" +
                    testUser.getTestName());
        }
    }

    @Test
    public void selectTestUserByPage() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        List<TestUser> testUsers = testMapper.selectTestUserByPage(new
                RowBounds());
        for (TestUser testUser : testUsers) {
            System.out.println("id:" + testUser.getId() + ",name:" +
                    testUser.getTestName());
        }
    }

    @Test
    public void deleteTestUser() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        int result = testMapper.deleteById(1L);
        System.out.println("result:" + result);
    }

    @Test
    public void deleteTestUserByWhereParams() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        WhereParamType whereParamType = new WhereParamType();
        Criteria criteria = whereParamType.createCriteria();
        criteria.andFieldIsEqualTo("id", 14);
        int result = testMapper.deleteByParams(whereParamType);
        System.out.println("result:" + result);
    }


    @Test
    public void selectTestUserToMap() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        Map<Long, TestUser> testUser = testMapper.selectTestUserToMap();
        System.out.println(testUser.size());
    }

    @Test
    public void selectTestUserByParamType() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        WhereParamType paramType = new WhereParamType();
        paramType.createCriteria().andFieldIsEqualTo("id", 6);
        List<TestUser> testUsers = testMapper.selectByParams(paramType);
        for (TestUser testUser : testUsers) {
            logger.info(testUser.getTestName());
        }
    }

    @Test
    public void selectPageByParams() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        WhereParamType paramType = new WhereParamType();
        paramType.createCriteria().andFieldIsEqualTo("id", 13L);
        List<TestUser> testUsers = testMapper.selectPageByParams(paramType, new RowBounds());
        for (TestUser testUser : testUsers) {
            System.out.println(testUser.getTestName());
        }
    }

    @Test
    public void selectEntityAll() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        List<TestUser> testUsers = testMapper.selectEntityAll();
        for (TestUser testUser : testUsers) {
            logger.info(testUser.getTestName());
        }
    }

    @Test
    public void selectById() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        TestUser testUser = testMapper.selectById(13);
        System.out.println(testUser.getTestName());
    }

    @Test
    public void updateTestUserByParamType() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        List<Object> list = new ArrayList<Object>();
        list.add(11L);
        int a = testMapper.updateTestUserByParamType("1212", list);
        System.out.println("dd:" + a);
    }

    @Test
    public void updateTestUserByWhereParam() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        WhereParamType paramType = new WhereParamType();
        List<Object> list = new ArrayList<Object>();
        list.add(1L);
        paramType.createCriteria().andFieldIdIn("id", list);
        paramType.put("testName", "updateTestName1");
        int a = testMapper.updateTestUserByWhereParam(paramType);
        System.out.println("dd:" + a);
    }
}
