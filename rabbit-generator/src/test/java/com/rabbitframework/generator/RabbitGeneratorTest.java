package com.rabbitframework.generator;

import java.io.Reader;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.core.utils.ResourceUtils;

public class RabbitGeneratorTest {
    private static final Logger logger = LoggerFactory.getLogger(RabbitGeneratorTest.class);

    @Test
    public void generator() throws Exception {
        Reader reader = ResourceUtils.getResourceAsReader("generator-config.xml");
        try {
            RabbitGeneratorBuilder builder = new RabbitGeneratorBuilder();
            RabbitGenerator rabbitGenerator = builder.build(reader);
            rabbitGenerator.generator();
        } finally {
            reader.close();
        }
    }
}
