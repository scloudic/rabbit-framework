package com.rabbitframework.web;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rabbitframework.commons.utils.StatusCode;

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
	 * 空值自动转换相匹配的类型属性
	 *
	 * @return
	 */
	public String toJson() {
		if (data != null && !json.containsKey("data")) {
			json.put("data", data);
		}
		return JSON.toJSONString(json, SerializerFeature.BrowserCompatible, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.SkipTransientField);
	}

	/**
	 * 空值自动过滤，不显示
	 *
	 * @return
	 */
	public String toJsonNoNull() {
		if (data != null && !json.containsKey("data")) {
			json.put("data", data);
		}
		return JSON.toJSONString(json, SerializerFeature.BrowserCompatible, SerializerFeature.SkipTransientField);
	}
}