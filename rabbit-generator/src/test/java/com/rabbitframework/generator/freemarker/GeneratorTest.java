package com.rabbitframework.generator.freemarker;
import org.junit.Before;
import org.junit.Test;

public class GeneratorTest {
    GeneratorConfig config = null;

    @Before
    public void before() {
        config = new GeneratorConfig();
    }

    @Test
    public void simle() {
        config.simple();
    }

    @Test
    public void list() {
        config.list();
    }
}
