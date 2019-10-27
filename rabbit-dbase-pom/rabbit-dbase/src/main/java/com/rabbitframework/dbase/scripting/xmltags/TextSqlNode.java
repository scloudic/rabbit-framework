package com.rabbitframework.dbase.scripting.xmltags;

import com.rabbitframework.dbase.mapping.SimpleTypeRegistry;
import com.rabbitframework.dbase.scripting.DynamicContext;
import com.rabbitframework.dbase.scripting.OgnlCache;
import com.tjzq.commons.propertytoken.GenericTokenParser;
import com.tjzq.commons.propertytoken.TokenHandler;

/**
 * 文本sql节点
 */
public class TextSqlNode implements SqlNode {
    private String text;

    public TextSqlNode(String text) {
        this.text = text;
    }

    @Override
    public boolean apply(DynamicContext dynamicContext) {
        GenericTokenParser parser = new GenericTokenParser("${", "}", new BindngTokenParser(dynamicContext));
        dynamicContext.appendSql(parser.parse(text));
        return true;
    }

    private static class BindngTokenParser implements TokenHandler {
        private DynamicContext dynamicContext;

        public BindngTokenParser(DynamicContext dynamicContext) {
            this.dynamicContext = dynamicContext;
        }

        @Override
        public String handleToken(String content) {
            Object parameter = dynamicContext.getBindings().get(DynamicContext.PARAMETER_OBJECT_KEY);
            if (parameter == null) {
                dynamicContext.getBindings().put("value", null);
            } else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) {
                dynamicContext.getBindings().put("value", parameter);
            }
            Object value = OgnlCache.getValue(content, dynamicContext.getBindings());
            return value == null ? "" : String.valueOf(value);
        }
    }
}
