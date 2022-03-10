package com.scloudic.rabbitframework.web.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.scloudic.rabbitframework.core.utils.CommonResponseUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scloudic.rabbitframework.core.exceptions.RabbitFrameworkException;
import com.scloudic.rabbitframework.core.exceptions.UnKnowException;
import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.web.DataJsonResponse;
import com.scloudic.rabbitframework.web.utils.ResponseUtils;
import com.scloudic.rabbitframework.web.utils.ServletContextHelper;
import com.scloudic.rabbitframework.core.utils.StringUtils;

import java.net.URI;

/**
 * 统一异常处理
 *
 * @author justin.liang
 */
public class ExceptionMapperSupport implements ExceptionMapper<Exception> {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionMapperSupport.class);
    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(Exception e) {
        logger.error(e.getMessage(), e);
        RabbitFrameworkException currException = null;
        if (e instanceof WebApplicationException) {
            WebApplicationException webException = ((WebApplicationException) e);
            Response response = webException.getResponse();
            int status = response.getStatus();
            Response resp = getResponseByStatus(status, null);
            if (resp != null) {
                response = resp;
            }
            return response;
        }
        if (e instanceof RabbitFrameworkException) {
            currException = (RabbitFrameworkException) e;
        } else {
            Throwable throwable = e.getCause();
            if (throwable != null && (throwable instanceof RabbitFrameworkException)) {
                currException = (RabbitFrameworkException) e.getCause();
            } else {
                currException = new UnKnowException(ServletContextHelper.getMessage("unknow.fail"), e);
            }
        }
        String message = ServletContextHelper.getMessage(currException.getMessage());
        return getResponseByStatus(currException.getStatus(), message);
    }

    public Response getResponseByStatus(StatusCode statusCode, String message) {
        Response response = null;
        int httpStatus = statusCode.getValue();
        int status = HttpServletResponse.SC_OK;
        try {
            switch (statusCode) {
                case SC_INTERNAL_SERVER_ERROR:
                case SC_UNAUTHORIZED:
                case SC_PROXY_AUTHENTICATION_REQUIRED:
                    response = getResponseByStatus(statusCode.getValue(), message);
                    break;
                case SC_LOGIN_ERROR:
                case FAIL:
                case SC_CACHE_ERROR:
                case SC_VALID_ERROR:
                    response = ResponseUtils.getResponse(status, getJson(httpStatus, getMessage(httpStatus, message)));
                    break;
                case SC_BIZ_ERROR:
                case SC_UN_KNOW:
                    if (isAsync()) {
                        response = ResponseUtils.getResponse(status, getJson(httpStatus, getMessage(httpStatus, message)));
                    } else {
                        response = Response.seeOther(new URI(CommonResponseUrl
                                .dislodgeFirstSlash(CommonResponseUrl.getOtherError()))).build();
                    }
                    break;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return response;
    }

    public Response getResponseByStatus(int httpStatus, String message) {
        Response response = null;
        int status = HttpServletResponse.SC_OK;
        try {
            switch (httpStatus) {
                case HttpServletResponse.SC_NOT_FOUND:
                    if (request != null) {
                        String url = request.getRequestURL().toString();
                        logger.warn("404错误地址：" + url);
                    }
                    if (!isAsync() && CommonResponseUrl.isPage404()) {
                        response = Response.seeOther(new URI(CommonResponseUrl.
                                dislodgeFirstSlash(CommonResponseUrl.getSys404ErrorUrl()))).build();
                    } else {
                        response = ResponseUtils.getResponse(status, getJson(httpStatus, getMessage(httpStatus, message)));
                    }
                    break;
                case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
                    if (isAsync()) {
                        response = ResponseUtils.getResponse(status, getJson(httpStatus, getMessage(httpStatus, message)));
                    } else {
                        response = Response.seeOther(new URI(CommonResponseUrl
                                .dislodgeFirstSlash(CommonResponseUrl.getSys500ErrorUrl()))).build();
                    }
                    break;
                case HttpServletResponse.SC_METHOD_NOT_ALLOWED:
                    if (isAsync()) {
                        response = ResponseUtils.getResponse(status, getJson(httpStatus, getMessage(httpStatus, message)));
                    } else {
                        response = Response.seeOther(new URI(CommonResponseUrl
                                .dislodgeFirstSlash(CommonResponseUrl.getSys405ErrorUrl()))).build();
                    }
                    break;
                case HttpServletResponse.SC_UNAUTHORIZED:
                    if (isAsync()) {
                        response = ResponseUtils.getResponse(status, getJson(httpStatus, getMessage(httpStatus, message)));
                    } else {
                        response = Response.seeOther(new URI(CommonResponseUrl
                                .dislodgeFirstSlash(CommonResponseUrl.getUnauthorizedUrl()))).build();
                    }
                    break;
                case HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED:
                    if (isAsync()) {
                        response = ResponseUtils.getResponse(status, getJson(httpStatus, getMessage(httpStatus, message)));
                    } else {
                        response = Response.seeOther(new URI(CommonResponseUrl
                                .dislodgeFirstSlash(CommonResponseUrl.getLoginUrl()))).build();
                    }
                    break;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return response;
    }

    private String getMessage(int status, String message) {
        if (StringUtils.isNotBlank(message)) {
            return message;
        }
        message = ServletContextHelper.getMessage(status + ".error");
        return message;
    }

    private boolean isAsync() {
        if (CommonResponseUrl.isFrontBlack()) {
            return true;
        }
        String requestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            return true;
        }
        return false;
    }

    private String getJson(int status, String message) {
        DataJsonResponse dataJsonResponse = new DataJsonResponse();
        dataJsonResponse.setStatus(status);
        dataJsonResponse.setMessage(message);
        return dataJsonResponse.toJson();
    }
}
