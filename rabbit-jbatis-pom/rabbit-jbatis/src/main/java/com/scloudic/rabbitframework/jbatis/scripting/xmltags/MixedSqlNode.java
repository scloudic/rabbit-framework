package com.scloudic.rabbitframework.jbatis.scripting.xmltags;

import com.scloudic.rabbitframework.jbatis.scripting.DynamicContext;

import java.util.List;

/**
 * 混合sql节点
 */
public class MixedSqlNode implements SqlNode {
    private List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext context) {
        for (SqlNode sqlNode : contents) {
            sqlNode.apply(context);
        }
        return true;
    }
}
