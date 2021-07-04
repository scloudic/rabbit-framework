package com.rabbitframework.core.httpclient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * http请求参数,目前只支持{@link String} 类型的参数
 *
 * @author justin
 */
public class RequestParams {
    private final ConcurrentHashMap<String, String> params = new ConcurrentHashMap<String, String>();

    public RequestParams() {
    }

    public RequestParams(Map<String, String> params) {
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            params.put(key, value);
        }
    }

    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : params.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }
        return result.toString();
    }

    public void clear() {
        params.clear();
    }

    /**
     * url+参数转换
     *
     * @param url
     * @return
     */
    public String getUrlWithStr(String url) {
        if (url == null)
            return null;

        url = url.replace(" ", "%20");

        if (this != null) {
            String paramString = toString().trim();
            if (!"".equals(paramString) && !"?".equals(paramString)) {
                url += url.contains("?") ? "&" : "?";
                url += paramString;
            }
        }
        return url;
    }
}
