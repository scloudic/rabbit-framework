package com.scloudic.rabbitframework.web.utils;

import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.web.filter.xss.XssHttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class WebUtils {
    private static Logger logger = LoggerFactory.getLogger(WebUtils.class);

    public static final String getRemoteAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (StringUtils.isNotBlank(ipAddress) && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                return ipAddress;
            } else if (ipAddress.length() == 15) {
                return ipAddress;
            }
        }

        ipAddress = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ipAddress) && !"unKnown".equalsIgnoreCase(ipAddress)) {
            return ipAddress;
        }
        return request.getRemoteAddr();
    }

    private static boolean isUnAvailableIp(String ip) {
        return (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip));
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = (HttpServletRequest) RequestContextHolder.currentRequestAttributes()
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
        return request;
    }

    public static HttpServletRequest getOrigRequest(HttpServletRequest request) {
        if (request == null) {
            request = getRequest();
        }
        if (request instanceof XssHttpServletRequestWrapper) {
            request = ((XssHttpServletRequestWrapper) request).getOrgRequest();
        }
        if (request == null) {
            try {
                if (request.getClass().getName().equals("com.scloudic.rabbitframework.security.web.servlet.SecurityHttpServletRequest")) {
                    Method method = request.getClass().getMethod("getOrgRequest");
                    request = (HttpServletRequest) method.invoke(request);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return request;
    }
}
