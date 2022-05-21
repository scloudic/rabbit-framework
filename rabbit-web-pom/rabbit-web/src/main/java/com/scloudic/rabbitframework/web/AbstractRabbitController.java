/**
 * Copyright 2009-2017 the original author or authors.<p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at<p>
 * http://www.apache.org/licenses/LICENSE-2.0<p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scloudic.rabbitframework.web;

import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.web.exceptions.ResourceException;
import com.scloudic.rabbitframework.web.utils.ServletContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRabbitController {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRabbitController.class);

    public String getMessage(String messageKey) {
        return ServletContextHelper.getMessage(messageKey);
    }

    public String getMessage(String messageKey, Object... args) {
        return ServletContextHelper.getMessage(messageKey, args);
    }

    public String getMessage(HttpServletRequest request, String messageKey) {
        return ServletContextHelper.getMessage(messageKey, request.getLocale());
    }

    public <T> Result<T> success(String message, T data) {
        return Result.success(message, data);
    }

    public <T> Result<T> success(T data) {
        return success(getMessage("success"), data);
    }

    public <T> Result<T> success() {
        return success(getMessage("success"), null);
    }

    public <T> Result<T> failure(StatusCode statusCode, T data) {
        return Result.failure(statusCode, data);
    }

    public <T> Result<T> failure(StatusCode statusCode) {
        return Result.failure(statusCode);
    }

    public <T> Result<T> failure(String message) {
        return Result.failure(message);
    }

    public <T> Result<T> failure() {
        return failure(getMessage("fail"));
    }

    public static <T> Result<T> failure(int status) {
        return Result.failure(status);
    }

    public static <T> Result<T> failure(int status, String message) {
        return Result.failure(status, message);
    }

    public String getHeader(HttpServletRequest request, String key) {
        return request.getHeader(key);
    }

    public List<Map<String, String>> getHeader(HttpServletRequest request, List<String> keys) {
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        for (String key : keys) {
            String value = getHeader(request, key);
            Map<String, String> map = new HashMap<String, String>();
            map.put(key, value);
            resultList.add(map);
        }
        return resultList;
    }

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

    protected Object getSessionAttValue(HttpServletRequest request, String att) {
        return getHttpSession(request).getAttribute(att);
    }


    protected void setContentType(HttpServletResponse response, String type) {
        response.setContentType(type);
    }

    /**
     * 设置上下文默认类型 默认为setContentType("text/json; charset=utf-8");
     *
     * @param response response
     */
    protected void setContentType(HttpServletResponse response) {
        setContentType(response, "text/json; charset=utf-8");
    }

    protected String getURLBase(HttpServletRequest request) {
        return request.getContextPath();
    }

    protected String getRealPath(HttpServletRequest request) {
        return request.getServletContext().getRealPath("/");
    }

    protected final HttpSession getHttpSession(HttpServletRequest request) {
        return request.getSession();
    }

    protected final boolean isAsync(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            return true;
        }
        return false;
    }
}
