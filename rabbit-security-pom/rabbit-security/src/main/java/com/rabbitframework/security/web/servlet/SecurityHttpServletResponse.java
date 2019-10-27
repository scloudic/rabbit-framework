package com.rabbitframework.security.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;
import org.apache.shiro.web.servlet.ShiroHttpSession;

public class SecurityHttpServletResponse extends ShiroHttpServletResponse {

	public SecurityHttpServletResponse(HttpServletResponse wrapped, ServletContext context,
			ShiroHttpServletRequest request) {
		super(wrapped, context, request);
	}

	@Override
	protected String toEncoded(String url, String sessionId) {
		if ((url == null) || (sessionId == null))
			return (url);

		String path = url;
		String query = "";
		String anchor = "";
		int question = url.indexOf('?');
		if (question >= 0) {
			path = url.substring(0, question);
			query = "&" + url.substring(question + 1);
		}
		int pound = path.indexOf('#');
		if (pound >= 0) {
			anchor = path.substring(pound);
			path = path.substring(0, pound);
		}
		StringBuilder sb = new StringBuilder(path);
		if (sb.length() > 0) { // session id param can't be first.
			sb.append("?");
			sb.append(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
			sb.append("=");
			sb.append(sessionId);
		}
		sb.append(anchor);
		sb.append(query);
		return (sb.toString());
	}
}
