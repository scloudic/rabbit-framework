package com.rabbitframework.web.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.commons.exceptions.RabbitFrameworkException;
import com.rabbitframework.commons.exceptions.UnKnowException;
import com.rabbitframework.commons.utils.StatusCode;
import com.rabbitframework.web.DataJsonResponse;
import com.rabbitframework.web.utils.ResponseUtils;
import com.rabbitframework.web.utils.ServletContextHelper;
import com.tjzq.commons.utils.JsonUtils;
import com.tjzq.commons.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 统一异常处理
 *
 * @author justin.liang
 */
@Provider
@Slf4j
public class ExceptionMapperSupport implements ExceptionMapper<Exception> {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionMapperSupport.class);
    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(Exception e) {
    	logger.error(e.getMessage(), e);
        int httpStatus = HttpServletResponse.SC_OK;
        DataJsonResponse dataJsonResponse = new DataJsonResponse();
        Exception currException = e;
        if (e instanceof WebApplicationException) {
            WebApplicationException webException = ((WebApplicationException) e);
            Response response = webException.getResponse();
            int status = response.getStatus();
            /// String message = webException.getMessage();
            // PrintWriter printWriter = null;
            // try {
            // Writer writer = new StringWriter();
            // printWriter = new PrintWriter(writer);
            // webException.printStackTrace(printWriter);
            // message = writer.toString();
            // } finally {
            // IOUtils.closeQuietly(printWriter);
            // }
            dataJsonResponse.setStatus(StatusCode.FAIL);
            dataJsonResponse.setMessage(ServletContextHelper.getMessage("request.error"));
            return ResponseUtils.getResponse(status, JsonUtils.toJson(dataJsonResponse));
        }
        if (!(e instanceof RabbitFrameworkException)) {
            Throwable throwable = e.getCause();
            if (throwable != null && (throwable instanceof RabbitFrameworkException)) {
                currException = (RabbitFrameworkException) e.getCause();
            } else {
                currException = new UnKnowException(ServletContextHelper.getMessage("unknow.fail"), e);
            }
        }

        RabbitFrameworkException rException = (RabbitFrameworkException) currException;
        String message = ServletContextHelper.getMessage(rException.getMessage());
        if (StringUtils.isBlank(message)) {
            message = rException.getMessage();
        }
        dataJsonResponse.setMessage(message);
        int status = rException.getStatus();
        int resultStatus = status;
        switch (status) {
            case StatusCode.SC_INTERNAL_SERVER_ERROR:
                httpStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                break;
            case StatusCode.SC_UNAUTHORIZED:
                httpStatus = HttpServletResponse.SC_UNAUTHORIZED;
                break;
//            case StatusCode.SC_PROXY_AUTHENTICATION_REQUIRED:
//                httpStatus = HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED;
//                break;
        }
        dataJsonResponse.setStatus(resultStatus);
        if (StringUtils.isBlank(message)) {
            message = ServletContextHelper.getMessage("fail");
        }
        dataJsonResponse.setMessage(message);
        return ResponseUtils.getResponse(httpStatus, dataJsonResponse.toJson());
    }
}