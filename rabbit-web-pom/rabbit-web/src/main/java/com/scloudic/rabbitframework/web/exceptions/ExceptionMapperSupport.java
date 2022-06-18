package com.scloudic.rabbitframework.web.exceptions;

import com.scloudic.rabbitframework.core.exceptions.RabbitFrameworkException;
import com.scloudic.rabbitframework.core.exceptions.UnKnowException;
import com.scloudic.rabbitframework.core.utils.CommonResponseUrl;
import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.web.Result;
import com.scloudic.rabbitframework.web.utils.ServletContextHelper;
import com.scloudic.rabbitframework.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 *
 * @author justin.liang
 */
@ControllerAdvice
public class ExceptionMapperSupport {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionMapperSupport.class);

    @ExceptionHandler(RabbitFrameworkException.class)
    @ResponseBody
    public Result rabbitFrameworkException(RabbitFrameworkException e,
                                           HandlerMethod handlerMethod,
                                           HttpServletResponse response,
                                           HttpServletRequest request) {
        logger.error("异常信息：" + e.getMessage(), e);
        logger.error("异常类：{}", handlerMethod.getBean().getClass());
        logger.error("异常请求地址：{}", request.getRequestURL());
        String msg = e.getDescription();
        if (StringUtils.isBlank(msg)) {
            msg = e.getMessage();
        }
        String message = ServletContextHelper.getMessage(msg);
        return getResponseByStatus(e.getStatus(), message, response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e, HandlerMethod handlerMethod,
                                   HttpServletRequest request) {
        logger.error("异常信息：" + e.getMessage(), e);
        logger.error("异常类：{}", handlerMethod.getBean().getClass()
                , handlerMethod.getMethod().getName());
        logger.error("异常请求地址：{}", request.getRequestURL());
        return Result.failure(StatusCode.SC_INTERNAL_SERVER_ERROR.getValue(), e.getMessage());
    }

    public Result getResponseByStatus(StatusCode statusCode, String message, HttpServletResponse response) {
        Result result = Result.failure(statusCode.getValue(), message);
        try {
            switch (statusCode) {
                case SC_INTERNAL_SERVER_ERROR:
                case SC_UNAUTHORIZED:
                case SC_PROXY_AUTHENTICATION_REQUIRED:
                    result = getResponseByStatus(statusCode.getValue(), message, response);
                    break;
                case SC_BIZ_ERROR:
                case SC_UN_KNOW:
                    if (!isAsync()) {
                        response.sendRedirect(CommonResponseUrl.
                                dislodgeFirstSlash(CommonResponseUrl.getOtherError()));
                    }
                    break;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return result;
    }

    public Result getResponseByStatus(int httpStatus, String message, HttpServletResponse response) {
        try {
            switch (httpStatus) {
                case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
                    if (!isAsync()) {
                        response.sendRedirect(CommonResponseUrl.
                                dislodgeFirstSlash(CommonResponseUrl.getSys500ErrorUrl()));
                    }
                    break;
                case HttpServletResponse.SC_METHOD_NOT_ALLOWED:
                    if (!isAsync()) {
                        response.sendRedirect(CommonResponseUrl.
                                dislodgeFirstSlash(CommonResponseUrl.getSys405ErrorUrl()));
                    }
                    break;
                case HttpServletResponse.SC_UNAUTHORIZED:
                    if (!isAsync()) {
                        response.sendRedirect(CommonResponseUrl.
                                dislodgeFirstSlash(CommonResponseUrl.getUnauthorizedUrl()));
                    }
                    break;
                case HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED:
                    if (!isAsync()) {
                        response.sendRedirect(CommonResponseUrl.
                                dislodgeFirstSlash(CommonResponseUrl.getLoginUrl()));
                    }
                    break;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return Result.failure(httpStatus, message);
    }

    private boolean isAsync() {
        if (CommonResponseUrl.isFrontBlack()) {
            return true;
        }
        String requestedWith = WebUtils.getRequest().getHeader("x-requested-with");
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            return true;
        }
        return false;
    }
}
