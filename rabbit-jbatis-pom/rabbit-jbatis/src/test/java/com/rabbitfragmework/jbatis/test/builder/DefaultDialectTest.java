package com.rabbitfragmework.jbatis.test.builder;

import com.rabbitframework.jbatis.dataaccess.dialect.DefaultDialect;
import org.junit.Test;

/**
 * @author: justin.liang
 * @date: 16/5/5 下午9:41
 */
public class DefaultDialectTest {
    @Test
    public void testDefaultDialect() {
        DefaultDialect defaultDialect = DefaultDialect.getDefaultDialect("mysql");
        System.out.println(defaultDialect.getDialect().getName());
    }
}
