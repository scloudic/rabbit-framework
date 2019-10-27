package com.rabbitframework.web.utils;

import javax.servlet.http.HttpServletRequest;

import com.tjzq.commons.utils.StringUtils;

public class WebUtils {
	/**
	 * 获取客户端ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (isUnAvailableIp(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (isUnAvailableIp(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (isUnAvailableIp(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private static boolean isUnAvailableIp(String ip) {
		return (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip));
	}
}
