package com.scloudic.rabbitframework.web;

import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class DataJsonResponse {
    private Map<String, Object> json = new HashMap<String, Object>();

    private Map<String, Object> data;

    public DataJsonResponse set(String key, Object value) {
        json.put(key, value);
        return this;
    }

    public DataJsonResponse setData(String key, Object value) {
        if (data == null) {
            data = new HashMap<String, Object>();
        }
        data.put(key, value);
        return this;
    }

    public DataJsonResponse setData(Object value) {
        json.put("data", value);
        return this;
    }

    public DataJsonResponse setMessage(String success) {
        json.put("message", success);
        return this;
    }

    public DataJsonResponse setStatus(boolean status, String message) {
        setStatus(status);
        json.put("message", message);
        return this;
    }

    public DataJsonResponse setStatus(boolean status) {
        json.put("status", status ? StatusCode.SC_OK : StatusCode.FAIL);
        return this;
    }

    public DataJsonResponse setStatus(int status) {
        json.put("status", status);
        return this;
    }

    public DataJsonResponse setStatus(int status, String message) {
        json.put("status", status);
        json.put("message", message);
        return this;
    }

    /**
     * 对象转字符串，空字段自动转换
     *
     * @return string
     */
    public String toJson() {
        return toJson(true);
    }

    /**
     * 根据参数将对象转换为json字符串
     *
     * @param isNullToEmpty 是否转换空字段值
     * @return string
     */
    public String toJson(boolean isNullToEmpty) {
        return toJson(isNullToEmpty, true);
    }

    /**
     * 根据参数将对象转换为json字符串
     * @param isNullToEmpty null 转空
     * @param isSkipTransientField 跳过
     * @return string
     */
    public String toJson(boolean isNullToEmpty, boolean isSkipTransientField) {
        if (data != null && !json.containsKey("data")) {
            json.put("data", data);
        }
        return JsonUtils.toJson(json, isNullToEmpty, isSkipTransientField);
    }
}