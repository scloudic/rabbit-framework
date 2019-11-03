package com.rabbitframework.jade.scripting.xmltags;

import com.rabbitframework.jade.scripting.DynamicContext;

/**
 * sql xml节点接口
 */
public interface SqlNode {
    boolean apply(DynamicContext context);
}
