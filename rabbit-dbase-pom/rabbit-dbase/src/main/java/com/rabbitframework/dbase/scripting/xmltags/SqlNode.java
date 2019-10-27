package com.rabbitframework.dbase.scripting.xmltags;

import com.rabbitframework.dbase.scripting.DynamicContext;

/**
 * sql xml节点接口
 */
public interface SqlNode {
    boolean apply(DynamicContext context);
}
