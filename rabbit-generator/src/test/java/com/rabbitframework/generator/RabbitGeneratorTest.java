package com.rabbitframework.generator;

import com.rabbitframework.commons.utils.ResourceUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;

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
