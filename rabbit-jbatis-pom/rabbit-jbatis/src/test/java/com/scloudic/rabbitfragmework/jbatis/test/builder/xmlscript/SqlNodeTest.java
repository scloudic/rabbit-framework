package com.scloudic.rabbitfragmework.jbatis.test.builder.xmlscript;

import com.scloudic.rabbitframework.core.xmlparser.XNode;
import com.scloudic.rabbitframework.core.xmlparser.XPathParser;
import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.exceptions.BuilderException;
import com.scloudic.rabbitframework.jbatis.mapping.BaseDefaultMethod;
import com.scloudic.rabbitframework.jbatis.scripting.DynamicContext;
import com.scloudic.rabbitframework.jbatis.scripting.sql.InsertByEntity;
import com.scloudic.rabbitframework.jbatis.scripting.xmltags.*;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void sqlNode() {
        InsertByEntity i = new InsertByEntity();
        String sqlValue = "update test_user set test_name=#{params.testName} where 1=1" + i.getSearchSql();
        String sqlBuilder = "<script>" +
                sqlValue +
                "</script>";

        XPathParser xPathParser = new XPathParser(sqlBuilder, null);
        XNode rootScriptNode = xPathParser.evalNode("/script");
        List<SqlNode> sqlNodes = parseDynamicTags(rootScriptNode);
//        MixedSqlNode rootSqlNode = new MixedSqlNode(sqlNodes);

    }


    private List<SqlNode> parseDynamicTags(XNode node) {
        List<SqlNode> contents = new ArrayList<SqlNode>();
        NodeList children = node.getNode().getChildNodes();  //直接通过w3c包获取NodeList
        int childrenLength = children.getLength();
        for (int i = 0; i < childrenLength; i++) {
            XNode child = node.newXNode(children.item(i));
            Node childNode = child.getNode();
            short nodeType = childNode.getNodeType();
            // <![CDATA[]]>中括着的纯文本，它没有子节点
            if (nodeType == Node.CDATA_SECTION_NODE
                    || nodeType == Node.TEXT_NODE) {
                String data = child.getStringBody("");
                contents.add(new TextSqlNode(data));
            }
        }
        return contents;
    }

}
