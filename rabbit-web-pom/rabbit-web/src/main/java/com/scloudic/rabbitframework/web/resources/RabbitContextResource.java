/**
 * Copyright 2009-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scloudic.rabbitframework.web.resources;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.scloudic.rabbitframework.web.exceptions.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scloudic.rabbitframework.core.utils.StringUtils;

public abstract class RabbitContextResource {
    private static final Logger logger = LoggerFactory.getLogger(RabbitContextResource.class);

    /**
     * 获取当前url路径
     *
     * @return
     * @throws Exception
     */
    protected String getCurrentUrl(HttpServletRequest request) throws ResourceException {
        String urlBase = request.getRequestURL().toString();
        String urlParameters = request.getQueryString();
        try {
            return URLEncoder.encode(urlBase + "?" + urlParameters, "UTF-8");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResourceException(e.getMessage(), e);
        }
    }

    /**
     * 根据参数获取session中的信息
     *
     * @param att
     * @return
     */
    protected Object getSessionAttValue(HttpServletRequest request, String att) {
        return getHttpSession(request).getAttribute(att);
    }

    /**
     * 根据参数设置上下文类型
     *
     * @param type
     */
    protected void setContentType(HttpServletResponse response, String type) {
        response.setContentType(type);
    }

    /**
     * 设置上下文默认类型 默认为setContentType("text/json; charset=utf-8");
     */
    protected void setContentType(HttpServletResponse response) {
        setContentType(response, "text/json; charset=utf-8");
    }

    /**
     * 获取当前工程名路径
     *
     * @return
     */
    protected String getURLBase(HttpServletRequest request) {
        return request.getContextPath();
    }

    /**
     * 获取当前工程详细路径
     *
     * @return
     */
    protected String getRealPath(HttpServletRequest request) {
        return request.getServletContext().getRealPath("/");
    }

    /**
     * 获取session
     *
     * @return
     */
    protected final HttpSession getHttpSession(HttpServletRequest request) {
        return request.getSession();
    }

    /**
     * 获取远程IP地址
     *
     * @param request
     * @return
     */
    protected final String getRemoteAddr(HttpServletRequest request) {
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

    /**
     * 是否异步执行
     *
     * @param request
     * @return
     */
    protected final boolean isAsync(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            return true;
        }
        return false;
    }

    // @Context
    // protected UriInfo uriInfo;
    //
    // @Context
    // protected Request restRequest;
    //
    // @Context
    // protected SecurityContext securityContext;
    //
    // @Context
    // protected HttpContext httpContext;
    //
    // @Context
    // protected CloseableService closeableService;
    //
    // @Context
    // protected HttpServletRequest request;
    //
    // @Context
    // protected HttpServletResponse response;
    //
    // @Context
    // protected ResourceContext resourceContext;
}
