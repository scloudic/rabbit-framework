package com.rabbitframework.dbase.springboot.test;

import com.rabbitframework.dbase.springboot.test.model.TestUser;
import com.rabbitframework.dbase.springboot.test.service.TestUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationDbaseMain.class)
public class ApplicationDbaseTest {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationDbaseTest.class);
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