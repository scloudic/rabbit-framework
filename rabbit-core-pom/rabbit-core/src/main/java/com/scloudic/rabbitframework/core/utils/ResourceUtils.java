package com.scloudic.rabbitframework.core.utils;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 资源加载共公类
 * 
 * @author Justin
 */
public class ResourceUtils extends MockResource {
	public static Properties getResourceAsProperties(String resource)
			throws IOException {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = getResourceAsStream(resource);
			props.load(in);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return props;
	}

	public static Reader getResourceAsReader(String resource)
			throws IOException {
		return getResourceAsReader(resource, null);
	}

	public static Reader getResourceAsReader(String resource, Charset charset)
			throws IOException {
		Reader reader;
		if (charset == null) {
			reader = new InputStreamReader(getResourceAsStream(resource));
		} else {
			reader = new InputStreamReader(getResourceAsStream(resource),
					charset);
		}
		return reader;
	}

	/**
	 * 根据URL获取数据并转换为 Properties
	 * 
	 * @param url url
	 * @return properties
	 * @throws IOException IOException
	 */
	public static Properties getUrlAsProperties(String url) throws IOException {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = getUrlAsInputStream(url);
			props.load(in);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return props;
	}

	/**
	 * 根据URL获取数据并转换为 InputStream
	 * 
	 * @param url url
	 * @return in
	 * @throws IOException IOException
	 */
	public static InputStream getUrlAsInputStream(String url)
			throws IOException {
		UrlResource urlResource = new UrlResource(url);
		InputStream inputStream = urlResource.getInputStream();
		return inputStream;
	}

}
