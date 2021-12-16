package com.scloudic.rabbitframework.core.httpclient;

import okhttp3.RequestBody;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
    private static HttpClient httpClient;

    static {
        httpClient = new HttpClient();
        httpClient.init();
    }

    public static ResponseBody get(String url) {
        return get(url, null);
    }

    public static ResponseBody get(String url, RequestParams params) {
        return get(url, params, null);
    }

    public static ResponseBody get(String url, RequestParams params, Map<String, String> headers) {
        return httpClient.get(url, params, headers);
    }

    public static ResponseBody post(String url, RequestParams params) {
        return post(url, params, null);
    }

    public static ResponseBody post(String url, RequestParams params, Map<String, String> headers) {
        return httpClient.post(url, params, headers);
    }

    public static ResponseBody fileUpload(String url, Map<String, List<File>> files, RequestParams params,
                                          Map<String, String> headers) {
        return httpClient.fileUpload(url, files, params, headers);
    }

    public static ResponseBody post(String url, String bodyStr, Map<String, String> headers, String contentType) {
        return httpClient.post(url, bodyStr, headers, contentType);
    }

    public static ResponseBody post(String url, String bodyStr, Map<String, String> headers) {
        return post(url, bodyStr, headers, null);
    }

    public static ResponseBody post(String url, RequestBody requestBody, Map<String, String> headers) {
        return httpClient.post(url, requestBody, headers);
    }

    public InputStream fileDownload(String url, RequestParams params, boolean isGet, Map<String, String> headers) {
        return httpClient.fileDownload(url, params, isGet, headers);
    }
}
