package com.scloudic.rabbitframework.web.test;

import com.scloudic.rabbitframework.core.utils.JsonUtils;
import org.junit.Test;
import org.owasp.esapi.ESAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class XssFilterTest {
    @Test
    public void esapiTest() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "{\"微信公众号\":\"weiXinMp\",\"微信小程序\":\"weiXinMini\"}");
        String value = ESAPI.encoder().canonicalize(JsonUtils.toJson(map));
        // 避免script 标签
        Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");
        // 避免src形式的表达式
        scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        // 删除单个的 </script> 标签
        scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");
        // 删除单个的<script ...> 标签
        scriptPattern = Pattern.compile("<script(.*?)>",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        // 避免 eval(...) 形式表达式
        scriptPattern = Pattern.compile("eval\\((.*?)\\)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        // 避免 e­xpression(...) 表达式
        scriptPattern = Pattern.compile("expression\\((.*?)\\)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        // 避免 javascript: 表达式
        scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");
        // 避免 vbscript:表达式
        scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
        value = scriptPattern.matcher(value).replaceAll("");
        // 避免 onload= 表达式
        scriptPattern = Pattern.compile("onload(.*?)=",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        //移除特殊标签
        value = value.replaceAll("<", "&lt;").replaceAll(">",
                "&gt;");
        // 避免 onXX= 表达式
        scriptPattern = Pattern.compile("on.*(.*?)=",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        System.out.println("结果：" + value);
    }
}
