package com.scloudic.rabbitframework.core.httpclient;

import com.scloudic.rabbitframework.core.utils.StringUtils;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.Proxy;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http请求,默认封装Okhttp
 *
 * @author justin
 */
public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private static final String ERROR_MSG_FORMAT = "请求失败,请求地址:%s,状态码:%s，错误信息:%s";
    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    public static final MediaType CONTENT_TYPE_FORM = MediaType
            .parse("application/x-www-form-urlencoded;charset=utf-8");
    public static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    private OkHttpClient okHttpClient;
    public static int CONNECT_TIME_OUT = 30;
    public static int WRITE_TIME_OUT = 30;
    public static int READ_TIME_OUT = 30;
    private int connectTimeout = CONNECT_TIME_OUT;
    private int writeTimeout = WRITE_TIME_OUT;
    private int redTimeout = READ_TIME_OUT;

    public void init() {
        init(null);
    }

    public void init(Proxy proxy) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS).readTimeout(redTimeout, TimeUnit.SECONDS);
        if (proxy != null) {
            builder.proxy(proxy);
        }
        okHttpClient = builder.build();
    }

    public ResponseBody get(String url) {
        return get(url, null);
    }

    public ResponseBody get(String url, RequestParams params) {
        return get(url, params, null);
    }

    public ResponseBody get(String url, RequestParams params, Map<String, String> headers) {
        if (null != params) {
            url = params.getUrlWithStr(url);
        }
        Request.Builder rb = new Request.Builder().url(url);
        setHeader(rb, headers);
        return sendRequest(rb.get().build());
    }

    public ResponseBody post(String url, RequestParams params) {
        return post(url, params, null);
    }

    public ResponseBody post(String url, RequestParams params, Map<String, String> headers) {
        RequestBody requestBody = buildPostFormRequest(params);
        return post(url, requestBody, headers);
    }

    public ResponseBody fileUpload(String url, Map<String, List<File>> files, RequestParams params,
                                   Map<String, String> headers) {
        RequestBody requestBody = buildMultipartFormRequest(files, params);
        return post(url, requestBody, headers);
    }

    public ResponseBody post(String url, String bodyStr, Map<String, String> headers) {
        return post(url, bodyStr, headers, null);
    }

    public ResponseBody post(String url, String bodyStr, Map<String, String> headers, String contentType) {
        MediaType mediaType = CONTENT_TYPE_JSON;
        if (StringUtils.isNotBlank(contentType)) {
            mediaType = MediaType.parse(contentType);
        }
        RequestBody body = RequestBody.create(mediaType, bodyStr);
        return post(url, body, headers);
    }

    public ResponseBody post(String url, RequestBody requestBody, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);
        builder.post(requestBody);
        setHeader(builder, headers);
        return sendRequest(builder.build());
    }

    public InputStream fileDownload(String url, RequestParams params, boolean isGet, Map<String, String> headers) {
        Request.Builder builder;
        if (isGet) {
            if (null != params) {
                url = params.getUrlWithStr(url);
            }
            builder = new Request.Builder().url(url).get();
        } else {
            builder = new Request.Builder().url(url).post(buildPostFormRequest(params));
        }
        setHeader(builder, headers);
        return sendRequest(builder.build()).inputStream();
    }

    /**
     * 文件上传封装 返回{@link RequestBody}
     *
     * @param files
     * @param params
     * @return
     */
    private RequestBody buildMultipartFormRequest(Map<String, List<File>> files, RequestParams params) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params != null) {
            Map<String, String> paramsMap = params.getParams();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        if (files != null) {
            RequestBody fileBody = null;
            for (Map.Entry<String, List<File>> fileEntry : files.entrySet()) {
                String name = fileEntry.getKey();
                List<File> fileList = fileEntry.getValue();
                if (fileList == null || fileList.size() <= 0) {
                    continue;
                }
                for (File file : fileList) {
                    String fileName = file.getName();
                    fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                    builder.addFormDataPart(name, fileName, fileBody);
                }

            }
        }
        RequestBody requestBody = builder.build();
        return requestBody;
    }

    /**
     * 表单封装 返回{@link RequestBody}
     *
     * @param params
     * @return
     */
    private RequestBody buildPostFormRequest(RequestParams params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            Map<String, String> paramsMap = params.getParams();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = builder.build();
        return requestBody;
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = MEDIA_TYPE_STREAM.toString();
        }
        return contentTypeFor;
    }

    private void setHeader(Request.Builder builder, Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private ResponseBody sendRequest(Request request) {
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                int code = response.code();
                String errorMsg = String.format(ERROR_MSG_FORMAT, response.protocol(), code, response.message());
                logger.error(errorMsg);
            }
            ResponseBody responseBody = new ResponseBody(response);
            return responseBody;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseBody put(String url, String bodyStr, Map<String, String> headers) {
        MediaType mediaType = CONTENT_TYPE_JSON;
        RequestBody body = RequestBody.create(mediaType, bodyStr);
        return put(url, body, headers);
    }

    public ResponseBody put(String url, RequestBody requestBody, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);
        builder.put(requestBody);
        setHeader(builder, headers);
        return sendRequest(builder.build());
    }

    public ResponseBody del(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);
        builder.delete();
        setHeader(builder, headers);
        return sendRequest(builder.build());
    }

    public ResponseBody del(String url, String bodyStr, Map<String, String> headers) {
        MediaType mediaType = CONTENT_TYPE_JSON;
        RequestBody body = RequestBody.create(mediaType, bodyStr);
        return del(url, body, headers);
    }

    public ResponseBody del(String url, RequestBody requestBody, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);
        builder.delete(requestBody);
        setHeader(builder, headers);
        return sendRequest(builder.build());
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public int getRedTimeout() {
        return redTimeout;
    }

    public void setRedTimeout(int redTimeout) {
        this.redTimeout = redTimeout;
    }

    public void cancelAll() {
        if (okHttpClient != null) {
            okHttpClient.dispatcher().cancelAll();
        }
    }
}