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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 *
 * @author justin.liang
 */
@ControllerAdvice
public class ExceptionMapperSupport {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionMapperSupport.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletResponse response,
                                   Exception e, HandlerMethod handlerMethod) {
        logger.error("异常信息：{}, {}", e.getMessage(), e);
        logger.error("异常类：{}, {}", handlerMethod.getBean().getClass()
                , handlerMethod.getMethod().getName());
        RabbitFrameworkException currException = null;
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
        return getResponseByStatus(currException.getStatus(), message, response);
    }

    public Result getResponseByStatus(StatusCode statusCode, String message, HttpServletResponse response) {
        Result result = Result.failure(statusCode.getValue(), message);
        ;
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
                case HttpServletResponse.SC_NOT_FOUND:
                    String url = WebUtils.getRequest().getRequestURL().toString();
                    logger.warn("404错误地址：" + url);
                    if (!isAsync() && CommonResponseUrl.isPage404()) {
                        response.sendRedirect(CommonResponseUrl.
                                dislodgeFirstSlash(CommonResponseUrl.getSys404ErrorUrl()));
                    }
                    break;
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
        String requestedWith = WebUtils.getRequest().getHeader("x-requested-with");
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            return true;
        }
        return false;
    }
}
