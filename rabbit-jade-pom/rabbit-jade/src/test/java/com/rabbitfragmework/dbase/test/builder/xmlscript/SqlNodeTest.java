package com.rabbitfragmework.dbase.test.builder.xmlscript;

import com.rabbitframework.jade.builder.Configuration;
import com.rabbitframework.jade.scripting.DynamicContext;
import com.rabbitframework.jade.scripting.xmltags.TextSqlNode;
import com.rabbitframework.jade.scripting.xmltags.TrimSqlNode;
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
