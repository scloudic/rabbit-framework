package com.scloudic.rabbitframework.core.httpclient;

import okhttp3.*;

import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.Proxy;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http请求,默认封装Okhttp
 * 
 * @author justin
 */
public class HttpClient {
	private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
	private static final MediaType CONTENT_TYPE_FORM = MediaType
			.parse("application/x-www-form-urlencoded;charset=utf-8");
	private static OkHttpClient okHttpClient;
	private static HttpClient httpClient = null;
	private static Object obj = new Object();
	public static int CONNECT_TIME_OUT = 30;
	public static int WRITE_TIME_OUT = 30;
	public static int READ_TIME_OUT = 30;

	private HttpClient() {
	}

	public static HttpClient getInstance() {
		if (httpClient == null) {
			synchronized (obj) {
				if (httpClient == null) {
					httpClient = new HttpClient();
					OkHttpClient.Builder builder = new OkHttpClient.Builder()
							.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
							.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
							.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
					okHttpClient = builder.build();
				}
			}
		}
		return httpClient;
	}

	public static HttpClient getInstance(Proxy proxy) {
		if (httpClient == null) {
			synchronized (obj) {
				if (httpClient == null) {
					httpClient = new HttpClient();
					OkHttpClient.Builder builder = new OkHttpClient.Builder()
							.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
							.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS).readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
							.proxy(proxy);
					okHttpClient = builder.build();
				}
			}
		}
		return httpClient;
	}

	public static HttpClient getInstance(int connectTimeOut, int writeTimeOut, int readTimeOut) {
		if (httpClient == null) {
			synchronized (obj) {
				if (httpClient == null) {
					httpClient = new HttpClient();
					OkHttpClient.Builder builder = new OkHttpClient.Builder()
							.connectTimeout(connectTimeOut, TimeUnit.SECONDS)
							.writeTimeout(writeTimeOut, TimeUnit.SECONDS).readTimeout(readTimeOut, TimeUnit.SECONDS);
					okHttpClient = builder.build();
				}
			}
		}
		return httpClient;
	}

	public static HttpClient getInstance(int connectTimeOut, int writeTimeOut, int readTimeOut, Proxy proxy) {
		if (httpClient == null) {
			synchronized (obj) {
				if (httpClient == null) {
					httpClient = new HttpClient();
					OkHttpClient.Builder builder = new OkHttpClient.Builder()
							.connectTimeout(connectTimeOut, TimeUnit.SECONDS)
							.writeTimeout(writeTimeOut, TimeUnit.SECONDS).readTimeout(readTimeOut, TimeUnit.SECONDS)
							.proxy(proxy);
					okHttpClient = builder.build();
				}
			}
		}
		return httpClient;
	}

	public static HttpClient getInstance(Proxy proxy, SSLSocketFactory sslSocketFactory) {
		if (httpClient == null) {
			synchronized (obj) {
				if (httpClient == null) {
					httpClient = new HttpClient();
					OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS)
							.writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS);
					if (proxy != null) {
						builder = builder.proxy(proxy);
					}
					if (sslSocketFactory != null) {
						builder = builder.sslSocketFactory(sslSocketFactory);
					}
					okHttpClient = builder.build();
				}
			}
		}
		return httpClient;
	}

	public com.scloudic.rabbitframework.core.httpclient.ResponseBody get(String url) {
		return get(url, null);
	}

	public com.scloudic.rabbitframework.core.httpclient.ResponseBody get(String url, RequestParams params) {
		return get(url, params, null);
	}

	public com.scloudic.rabbitframework.core.httpclient.ResponseBody get(String url, RequestParams params, Map<String, String> headers) {
		if (null != params) {
			url = params.getUrlWithStr(url);
		}
		Request.Builder rb = new Request.Builder().url(url);
		setHeader(rb, headers);
		return sendRequest(rb.get().build());
	}

	public com.scloudic.rabbitframework.core.httpclient.ResponseBody post(String url, RequestParams params) {
		return post(url, params, null);
	}

	public com.scloudic.rabbitframework.core.httpclient.ResponseBody post(String url, RequestParams params, Map<String, String> headers) {
		RequestBody requestBody = buildPostFormRequest(params);
		return post(url, requestBody, headers);
	}

	/**
	 * 文件上传/表单参数
	 *
	 * @param url
	 * @param files
	 * @param params
	 * @param responseHandler
	 *            * @param tag
	 * @return
	 */
	public com.scloudic.rabbitframework.core.httpclient.ResponseBody fileUpload(String url, Map<String, List<File>> files, RequestParams params,
                                                                                Map<String, String> headers) {
		RequestBody requestBody = buildMultipartFormRequest(files, params);
		return post(url, requestBody, headers);
	}

	public com.scloudic.rabbitframework.core.httpclient.ResponseBody post(String url, String bodyStr, Map<String, String> headers, String contentType) {
		MediaType mediaType = CONTENT_TYPE_FORM;
		if (contentType != null && !"".equals(contentType)) {
			mediaType = MediaType.parse(contentType);
		}
		RequestBody body = RequestBody.create(mediaType, bodyStr);
		return post(url, body, headers);
	}

	public com.scloudic.rabbitframework.core.httpclient.ResponseBody post(String url, String bodyStr, Map<String, String> headers) {
		return post(url, bodyStr, headers, null);
	}

	public com.scloudic.rabbitframework.core.httpclient.ResponseBody post(String url, RequestBody requestBody, Map<String, String> headers) {
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

	// public String postSSL(String url, RequestParams params, String certPath,
	// String certPass) {
	// okhttp3.Request request = new
	// okhttp3.Request.Builder().url(url).post(buildPostFormRequest(params)).build();
	// InputStream inputStream = null;
	// try {
	// KeyStore clientStore = KeyStore.getInstance("PKCS12");
	// inputStream = new FileInputStream(certPath);
	// char[] passArray = certPass.toCharArray();
	// clientStore.load(inputStream, passArray);
	// KeyManagerFactory kmf =
	// KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	// kmf.init(clientStore, passArray);
	// KeyManager[] kms = kmf.getKeyManagers();
	// SSLContext sslContext = SSLContext.getInstance("TLSv1");
	// sslContext.init(kms, null, new SecureRandom());
	// okhttp3.OkHttpClient httpsClient = new
	// okhttp3.OkHttpClient().newBuilder()
	// .connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
	// .readTimeout(30,
	// TimeUnit.SECONDS).sslSocketFactory(sslContext.getSocketFactory()).build();
	// okhttp3.Response response = httpsClient.newCall(request).execute();
	// if (!response.isSuccessful())
	// throw new RuntimeException("Unexpected code " + response);
	// return response.body().string();
	// } catch (Exception e) {
	// throw new RuntimeException(e);
	// } finally {
	// IOUtils.closeQuietly(inputStream);
	// }
	// }

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
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				builder.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}

	private com.scloudic.rabbitframework.core.httpclient.ResponseBody sendRequest(Request request) {
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			if (!response.isSuccessful()) {
				throw new RuntimeException("Unexpected code " + response);
			}
			com.scloudic.rabbitframework.core.httpclient.ResponseBody responseBody = new ResponseBody();
			responseBody.setResponse(response);
			return responseBody;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void close() {
		httpClient = null;
		okHttpClient = null;
	}
}