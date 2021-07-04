package com.rabbitframework.jbatis.scripting.xmltags;

import com.rabbitframework.jbatis.scripting.DynamicContext;

import java.util.List;

/**
 * choose节点
 *
 */
public class ChooseSqlNode implements SqlNode {
    private SqlNode defaultSqlNode;
    private List<SqlNode> ifSqlNodes;

    public ChooseSqlNode(List<SqlNode> ifSqlNodes, SqlNode defaultSqlNode) {
        this.ifSqlNodes = ifSqlNodes;
        this.defaultSqlNode = defaultSqlNode;
    }

    public boolean apply(DynamicContext context) {
        for (SqlNode sqlNode : ifSqlNodes) {
            if (sqlNode.apply(context)) {
                return true;
            }
        }
        if (defaultSqlNode != null) {
            defaultSqlNode.apply(context);
            return true;
        }
        return false;
    }
}
