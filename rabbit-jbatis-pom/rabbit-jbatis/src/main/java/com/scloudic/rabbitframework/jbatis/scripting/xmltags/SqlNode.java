package com.scloudic.rabbitframework.jbatis.scripting.xmltags;

import com.scloudic.rabbitframework.jbatis.scripting.DynamicContext;

/**
 * sql xml节点接口
 */
public interface SqlNode {
    boolean apply(DynamicContext context);
}
