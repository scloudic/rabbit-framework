package com.scloudic.rabbitframework.jbatis.springboot.test;

import com.alibaba.fastjson.JSON;
import com.scloudic.rabbitframework.core.utils.PageBean;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.mapping.lambda.SFunctionUtils;
import com.scloudic.rabbitframework.jbatis.mapping.param.Criteria;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;
import com.scloudic.rabbitframework.jbatis.springboot.test.mapper.TestUserMapper;
import com.scloudic.rabbitframework.jbatis.springboot.test.model.TestUser;
import com.scloudic.rabbitframework.jbatis.springboot.test.model.User;
import com.scloudic.rabbitframework.jbatis.springboot.test.service.TestUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationJbatisMain.class)
public class ApplicationJbatisTest {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationJbatisTest.class);
    @Autowired
    private TestUserService testUserService;
    @Autowired
    private TestUserMapper testUserMapper;

    @Test
    public void createTable() {
        testUserService.createTable();
    }

    @Test
    public void insertByEntity() {
        TestUser testUser = new TestUser();
        testUser.setCreateTime(new Date());
        int result = testUserService.insertByEntity(testUser);
        logger.info("插入获取的Id主键:" + testUser.getId() + ",返回值：" + result);
    }


    @Test
    public void insertDynamicTable() {
        TestUser testUser = new TestUser();
        testUser.setCreateTime(new Date());
        int result = testUserService.insertDynamicTable(testUser, "_2021");
        logger.info("插入获取的Id主键:" + testUser.getId() + ",返回值：" + result);
    }

    @Test
    public void batchInsertEntity() {
        List<TestUser> tu = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            TestUser testUser = new TestUser();
            testUser.setTestName("批插入" + i);
            testUser.setCreateTime(new Date());
            tu.add(testUser);
        }
        int result = testUserService.batchInsertEntity(tu);
        logger.info("返回值：" + result);
        for (TestUser t : tu) {
            logger.info("插入获取的Id主键:" + t.getId());
        }
    }

    @Test
    public void deleteById() {
        int result = testUserService.deleteById(5);
        logger.info("删除返回的结果：" + result);
    }

    @Test
    public void deleteDynamicTableById() {
        int result = testUserService.deleteDynamicTableById(1, "_2021");
        logger.info("删除返回的结果：" + result);
    }

    @Test
    public void deleteByParams() {
        Where where = new Where();
        Criteria criteria = where.createCriteria();
        where.setTableSuffix("11");
        criteria.andEqual(TestUser::getId, 4);
        int result = testUserService.deleteByParams(where);
        logger.info("删除返回的结果：" + result);
    }

    @Test
    public void updateByEntity() {
        TestUser testUser = new TestUser();
        testUser.setId(6);
        testUser.setTestName("修改的记录");
        testUserService.updateByEntity(testUser);
    }

    @Test
    public void insertTestUserRollback() {
        testUserService.insertTestUserRollback();

    }

    @Test
    public void updateByParams() {
        Where where = new Where();
        where.put(SFunctionUtils.getFieldPropertyName(TestUser::getTestName), "where修改2");
        where.put(SFunctionUtils.getFieldPropertyName(TestUser::getCreateTime), new Date());
        Criteria criteria = where.createCriteria();
        criteria.andEqual(TestUser::getId, 1);
        int result = testUserService.updateByParams(where);
        logger.info("修改返回的结果：" + result);
    }

    @Test
    public void selectById() {
        TestUser testUser = testUserService.selectById(5);
        if (testUser != null) {
            logger.info(testUser.getTestName());
        } else {
            logger.info("无数据显示");
        }
    }

    @Test
    public void selectDynamicTableById() {
        TestUser testUser = testUserService.selectDynamicTableById(5, "11");
        if (testUser != null) {
            logger.info(testUser.getTestName());
        } else {
            logger.info("无数据显示");
        }
    }

    @Test
    public void selectByParams() {
        Where where = new Where();
//        where.setTableSuffix("11");
        Criteria criteria = where.createCriteria();
        criteria.andLike(TestUser::getTestName, "%插入%");
        criteria.orEqual(TestUser::getId, 2);
        criteria.andEqual(TestUser::getId, 1);
        List<TestUser> testUsers = testUserService.selectByParams(where);
        for (TestUser testUser : testUsers) {
            logger.info(testUser.getTestName());
        }
    }

    @Test
    public void selectCountByParams() {
        Where where = new Where();
        //where.setTableSuffix("11");
        Criteria criteria = where.createCriteria();
        criteria.andLike(TestUser::getTestName, "%插入%");
        criteria.orEqual(TestUser::getId, 2);
        criteria.andEqual(TestUser::getId, 1);
        long count = testUserService.selectCountByParams(where);
        logger.info("总数：" + count);
    }

    @Test
    public void selectCount() {
        long count = testUserService.selectCount();
        logger.info("总数：" + count);
    }

    @Test
    public void selectDynamicTableCount() {
        long count = testUserService.selectDynamicTableCount("11");
        logger.info("总数：" + count);
    }


    @Test
    public void selectEntityAll() {
        List<TestUser> testUsers = testUserService.selectEntityAll();
        for (TestUser testUser : testUsers) {
            logger.info(testUser.getTestName());
        }
    }

    @Test
    public void selectDynamicTableEntityAll() {
        List<TestUser> testUsers = testUserService.selectDynamicTableEntityAll("11");
        for (TestUser testUser : testUsers) {
            logger.info(testUser.getTestName());
        }
    }

    @Test
    public void selectPageByParams() {
        Where where = new Where();
        where.setTableSuffix("11");
        Criteria criteria = where.createCriteria();
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(6);
        criteria.andIn(TestUser::getId, integers);
        List<TestUser> testUsers = testUserService.selectPageByParams(where, new RowBounds(0, 3));
        for (TestUser testUser : testUsers) {
            logger.info(testUser.getTestName());
        }
    }

    @Test
    public void selectEntityPage() {
        List<TestUser> testUsers = testUserService.selectEntityPage(new RowBounds(0, 3));
        for (TestUser testUser : testUsers) {
            logger.info(testUser.getTestName());
        }
    }

    @Test
    public void selectDynamicTableEntityPage() {
        List<TestUser> testUsers = testUserService.selectDynamicTableEntityPage(new RowBounds(0, 3), "11");
        for (TestUser testUser : testUsers) {
            logger.info(testUser.getTestName());
        }
    }

    @Test
    public void selectOneByParams() {
        Where where = new Where();
        where.setTableSuffix("11");
        Criteria criteria = where.createCriteria();
        criteria.andEqual(TestUser::getId, 1);
        TestUser testUser = testUserService.selectOneByParams(where);
        if (testUser != null) {
            logger.info(testUser.getTestName());
        } else {
            logger.info("无数据显示");
        }
    }

    @Test
    public void selectPageBeanByParams() {
        Where where = new Where();
        Criteria criteria = where.createCriteria();
        criteria.andEqual(TestUser::getId, 1);
        PageBean<TestUser> testUserPageBean = testUserService.selectPageBeanByParams(where, 0L, 10L);
        logger.info("数据结果：" + JSON.toJSONString(testUserPageBean));
    }

    @Test
    public void updateTest() {
        testUserMapper.updateTest(1, "测试");
    }

    @Test
    public void selectTestUser() {
        List<User> users = testUserMapper.selectTestUser();
        for (User testUser : users) {
            logger.info(testUser.getTestName());
        }
    }

    @Test
    public void selectTestUserToMap() {
        Map<Long, User> map = testUserMapper.selectTestUserToMap();
        for (Map.Entry<Long, User> entry : map.entrySet()) {
            logger.info("id:" + entry.getKey() + ",name:" + entry.getValue().getTestName());
        }
    }

    @Test
    public void selectTestUserByPage() {
        List<User> users = testUserMapper.selectTestUserByPage(new RowBounds(0, 3));
        for (User testUser : users) {
            logger.info(testUser.getTestName());
        }
    }

    @Test
    public void selectTestUserWhere() {
        Where where = new Where();
        Criteria criteria = where.createCriteria();
        criteria.andEqual("id", 1);
        List<User> users = testUserMapper.selectTestUserWhere(where);
        for (User testUser : users) {
            logger.info(testUser.getTestName());
        }
    }

    @Test
    public void updateTestUserByParamType() {
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(6);
        testUserMapper.updateTestUserByParamType("测试update", integers);
    }

    @Test
    public void updateTestUserByWhereParam() {
        Where where = new Where();
        where.put(SFunctionUtils.getFieldPropertyName(TestUser::getTestName), "where修改2");
        Criteria criteria = where.createCriteria();
        criteria.andEqual(TestUser::getId, 2);
        testUserMapper.updateTestUserByWhereParam(where);
    }


    @Test
    public void updateDynamicTable() {
        TestUser testUser = new TestUser();
        testUser.setId(1);
        testUser.setTestName("updateDynamicTable");
        testUserMapper.updateDynamicTable(testUser, "11");
    }

    @Test
    public void delTestUserById() {
        int result = testUserMapper.delTestUserById(6L);
        logger.info("删除返回的结果：" + result);
    }

    @Test
    public void delTestUserWhere() {
        Where where = new Where();
        Criteria criteria = where.createCriteria();
        criteria.andEqual(TestUser::getId, 2);
        int result = testUserMapper.delTestUserWhere(where);
        logger.info("删除返回的结果：" + result);
    }

}

