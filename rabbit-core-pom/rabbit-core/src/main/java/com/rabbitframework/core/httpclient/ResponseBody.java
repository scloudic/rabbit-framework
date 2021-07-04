package com.rabbitframework.core.httpclient;

import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class ResponseBody {
	private static final Logger logger = LoggerFactory.getLogger(ResponseBody.class);
	private Response response;
	private okhttp3.ResponseBody okResponseBody;

	public void setResponse(Response response) {
		this.response = response;
		okResponseBody = response.body();
	}

	public String string() {
		try {
			return okResponseBody.string();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new HttpException("responseBody transform error! ");
		} finally {
			close();
		}
	}

	public InputStream inputStream() {
		try {
			InputStream inputStream = okResponseBody.byteStream();
			return inputStream;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new HttpException("responseBody transform error! ");
		} finally {
			close();
		}
	}

	public byte[] bytes() {
		try {
			return okResponseBody.bytes();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new HttpException("responseBody transform error! ");
		} finally {
			close();
		}
	}

	private void close() {
		response.close();
	}
}