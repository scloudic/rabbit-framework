package com.rabbitframework.jbatis.scripting;

import com.rabbitframework.jbatis.exceptions.BuilderException;
import com.rabbitframework.jbatis.scripting.xmltags.OgnlClassResolver;
import ognl.ExpressionSyntaxException;
import ognl.Ognl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ognl缓存类
 */
public class OgnlCache {
    private static final Map<String, Object> expCache = new ConcurrentHashMap<String, Object>();

    private OgnlCache() {
    }

    public static Object getValue(String exp, Object root) {
        try {
            Map<Object, OgnlClassResolver> context = Ognl.createDefaultContext(root, new OgnlClassResolver());
            return Ognl.getValue(getExp(exp), context, root);
        } catch (Exception e) {
            throw new BuilderException("Error evaluating expression '" + exp + "'. Cause: " + e);
        }
    }

    private static Object getExp(String exp) throws Exception {
        try {
            Object node = expCache.get(exp);
            if (node == null) {
//                node = new OgnlParser(new StringReader(exp)).topLevelExpression();
                node = Ognl.parseExpression(exp);
                expCache.put(exp, node);
            }
            return node;
        } catch (Exception e) {
            throw new ExpressionSyntaxException(exp, e);
        }
    }
}
