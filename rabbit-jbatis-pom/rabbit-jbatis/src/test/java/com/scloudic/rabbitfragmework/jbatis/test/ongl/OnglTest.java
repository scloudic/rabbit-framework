package com.scloudic.rabbitfragmework.jbatis.test.ongl;

import ognl.Node;
import ognl.Ognl;
import ognl.OgnlParser;

import java.io.StringReader;
import java.util.HashMap;

public class OnglTest {
    private final ContextMap bindings;

    public OnglTest() {
        bindings = new ContextMap();
        bindings.put("abc", new TestBean("1", "liangjy"));
    }

    public ContextMap getBindings() {
        return bindings;
    }

    static class TestBean {
        private String id;
        private String name;

        public TestBean(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class ContextMap extends HashMap<String, Object> {

        @Override
        public Object put(String key, Object value) {
            return super.put(key, value);
        }

        @Override
        public Object get(Object key) {
            return super.get(key);
        }
    }

    public static void main(String[] args) throws Exception {
        OnglTest onglTest = new OnglTest();
        Node node = new OgnlParser(new StringReader("abc.id")).topLevelExpression();
        Object value = Ognl.getValue(node, onglTest.getBindings());
        System.out.println("value:" + value);
    }

}
