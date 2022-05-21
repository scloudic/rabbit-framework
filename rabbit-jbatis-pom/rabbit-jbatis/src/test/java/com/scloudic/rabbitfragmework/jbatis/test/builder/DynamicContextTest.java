package com.scloudic.rabbitfragmework.jbatis.test.builder;


import com.scloudic.rabbitframework.jbatis.builder.Configuration;
import com.scloudic.rabbitframework.jbatis.mapping.SimpleTypeRegistry;
import com.scloudic.rabbitframework.jbatis.scripting.DynamicContext;
import com.scloudic.rabbitframework.jbatis.scripting.OgnlCache;

import java.util.HashMap;
import java.util.Map;

public class DynamicContextTest {
    public static void main(String[] args) {
        String content = "content";
//        DynamicContext dynamicContext = getDynamicContextSetMap();
//        DynamicContext dynamicContext = getDynamicContextSetBean();
        DynamicContext dynamicContext = getDynamicContextSetValue();
        Object parameter = dynamicContext.getBindings().get(DynamicContext.PARAMETER_OBJECT_KEY);
        if (parameter == null) {
            dynamicContext.getBindings().put("value", null);
        } else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) {
            dynamicContext.getBindings().put("value", parameter);
        }
        Object value = OgnlCache.getValue(content, dynamicContext.getBindings());
        System.out.println("value:" + value);
    }

    public static DynamicContext getDynamicContextSetMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content", "getDynamicContextSetMap");
        DynamicContext dynamicContext = new DynamicContext(null, map);
        return dynamicContext;
    }

    public static DynamicContext getDynamicContextSetBean() {
        Test test = new Test();
        test.setContent("getDynamicContextSetBean");
        DynamicContext dynamicContext = new DynamicContext(new Configuration(), test);
        return dynamicContext;
    }

    public static DynamicContext getDynamicContextSetValue() {

        DynamicContext dynamicContext = new DynamicContext(new Configuration(), "getDynamicContextSetValue");
        return dynamicContext;
    }


    private static class Test {
        public String content;

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}
