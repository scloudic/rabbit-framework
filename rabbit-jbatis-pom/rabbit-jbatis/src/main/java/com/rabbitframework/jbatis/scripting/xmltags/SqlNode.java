package com.rabbitframework.jbatis.scripting.xmltags;

import com.rabbitframework.jbatis.scripting.DynamicContext;

/**
 * sql xml节点接口
 */
public interface SqlNode {
    boolean apply(DynamicContext context);
}
