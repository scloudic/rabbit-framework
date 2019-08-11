package com.rabbitframework.commons.codec;

import com.rabbitframework.commons.utils.DigestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Md5HashTest {
    private static final Logger logger = LoggerFactory.getLogger(Md5HashTest.class);

    @Test
    public void md5Hash() {
        logger.info("md5:" + DigestUtils.md5ToStr("123456"));
    }
}
