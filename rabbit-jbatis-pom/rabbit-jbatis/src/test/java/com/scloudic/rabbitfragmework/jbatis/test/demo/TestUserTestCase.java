package com.scloudic.rabbitfragmework.jbatis.test.demo;

import com.scloudic.rabbitfragmework.jbatis.test.core.DataAccessTestCase;
import com.scloudic.rabbitfragmework.jbatis.test.mapper.TestUserMapper;
import com.scloudic.rabbitfragmework.jbatis.test.model.TestUser;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.mapping.param.Criteria;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;
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
//        testUser.setId(22L);
        testUser.setTestName("test1");
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
        int result = testMapper.batchInsertEntity(testUsers);
        System.out.println(result);
    }


    @Test
    public void updateTestUserById() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        int result = testMapper.updateTest(5, "updateName");
        System.out.println("result:" + result);
    }

    @Test
    public void updateTestByUser() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        TestUser testUser = new TestUser();
        testUser.setId(6L);
        testUser.setTestName("updateTestByUser");
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
        Where whereParamType = new Where();
        Criteria criteria = whereParamType.createCriteria();
        criteria.andEqual("id", 14);
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
        Where paramType = new Where("test_name");
        paramType.createCriteria().andEqual("id", 6);
        List<TestUser> testUsers = testMapper.selectByParams(paramType);
        for (TestUser testUser : testUsers) {
            logger.info("aaa:" + testUser.getTestName());
        }
    }

    @Test
    public void selectPageByParams() {
        TestUserMapper testMapper = getMapper(TestUserMapper.class);
        Where paramType = new Where();
        paramType.createCriteria()
                .orEqual("id", 13L)
                .orEqual("test_name", "1");
        paramType.addCreateCriteria()
                .andEqual("id", 1L).andEqual("test_name", "333");
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
        Where paramType = new Where();
        List<Object> list = new ArrayList<Object>();
        list.add(1L);
        paramType.createCriteria().andIn("id", list);
        paramType.putProperty(TestUser::getTestName,"test");
        int a = testMapper.updateByParams(paramType);
        System.out.println("dd:" + a);
    }
}