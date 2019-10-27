package com.rabbitframework.security.springboot.configure.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationSecurityMain.class)
public class ApplicationSecurityTest {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationSecurityTest.class);
    
    @Test
    public void testConfig() {
        logger.debug("testConfig");
    }
}