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
