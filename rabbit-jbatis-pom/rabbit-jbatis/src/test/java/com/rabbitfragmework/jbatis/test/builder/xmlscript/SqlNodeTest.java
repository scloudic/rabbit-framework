package com.rabbitfragmework.jbatis.test.builder.xmlscript;

import com.rabbitframework.jbatis.builder.Configuration;
import com.rabbitframework.jbatis.scripting.DynamicContext;
import com.rabbitframework.jbatis.scripting.xmltags.TextSqlNode;
import com.rabbitframework.jbatis.scripting.xmltags.TrimSqlNode;
import org.junit.Test;

public class SqlNodeTest {
    @Test
    public void testTrimSqlNode() {
        Configuration configuration = new Configuration();
        DynamicContext dynamicContext = new DynamicContext(configuration, null);
        TextSqlNode textSqlNode = new TextSqlNode("and insert into");
        TrimSqlNode trimSqlNode = new TrimSqlNode(configuration, textSqlNode, "(", null, ")", null);
        trimSqlNode.apply(dynamicContext);
        System.out.println(dynamicContext.getSql());

    }
}
