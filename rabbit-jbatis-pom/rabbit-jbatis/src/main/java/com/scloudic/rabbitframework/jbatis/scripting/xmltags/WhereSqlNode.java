package com.scloudic.rabbitframework.jbatis.scripting.xmltags;

import com.scloudic.rabbitframework.jbatis.builder.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * where节点继承{@link TrimSqlNode}
 * where节点没有属性:如:&lt;where&gt;&lt;/where&gt;
 */
public class WhereSqlNode extends TrimSqlNode {
    private static List<String> prefixList = Arrays.asList("AND ", "OR ", "AND\n", "OR\n", "AND\r", "OR\r", "AND\t", "OR\t");

    public WhereSqlNode(Configuration configuration, SqlNode contents, String prefix) {
        super(configuration, contents, prefix, prefixList, null, null);
    }
}
