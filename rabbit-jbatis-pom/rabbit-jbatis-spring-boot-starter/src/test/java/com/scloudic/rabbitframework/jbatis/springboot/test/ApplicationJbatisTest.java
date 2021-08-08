package com.scloudic.rabbitframework.jbatis.springboot.test;

import com.scloudic.rabbitframework.jbatis.springboot.test.model.TestUser;
import com.scloudic.rabbitframework.jbatis.springboot.test.service.TestUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationJbatisMain.class)
public class ApplicationJbatisTest {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationJbatisTest.class);
    @Autowired
    private TestUserService testUserService;

    @Test
    public void testSelect() {
        logger.debug("testConfig method load:" + testUserService);
        List<TestUser> testUsers = testUserService.selectTestUser();
        testUsers.forEach((testUser) -> {
            System.out.println("testName:" + testUser.getTestName());
        });
    }


    @Test
    public void testInsert() {
        testUserService.insertTestUserRollback();
    }
}