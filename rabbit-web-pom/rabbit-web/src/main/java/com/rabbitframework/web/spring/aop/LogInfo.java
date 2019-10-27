package com.rabbitframework.web.spring.aop;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志信息
 *
 * @author: justin
 * @date: 2017-06-13 16:27
 */
public class LogInfo {
    public String methodName;
    private Map<String, Object> value = new HashMap<String, Object>();


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }
}
