package com.rabbitframework.web;

import com.rabbitframework.commons.utils.StatusCode;
import com.rabbitframework.web.resources.RabbitContextResource;
import com.rabbitframework.web.utils.ResponseUtils;
import com.rabbitframework.web.utils.ServletContextHelper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * rest抽象接口类
 *
 * @author: justin.liang
 */
public abstract class AbstractContextResource extends RabbitContextResource {
    public String getMessage(String messageKey) {
        return ServletContextHelper.getMessage(messageKey);
    }

    public String getMessage(String messageKey, Object... args) {
        return ServletContextHelper.getMessage(messageKey, args);
    }

    public String getMessage(HttpServletRequest request, String messageKey) {
        return ServletContextHelper.getMessage(messageKey, request.getLocale());
    }

    public Response getSimpleResponse(boolean result) {
        return getSimpleResponse(result, null);
    }

    public Response getSimpleResponse(boolean result, Object data) {
        return getSimpleResponse(result, data, true);
    }

    /**
     * 获取Response返回信息
     *
     * @param result
     * @param data
     * @param isNullToEmpty 是否空字段值转换
     * @return
     */
    public Response getSimpleResponse(boolean result, Object data, boolean isNullToEmpty) {
        DataJsonResponse dataJsonResponse = new DataJsonResponse();
        if (data != null) {
            dataJsonResponse.setData(data);
        }
        dataJsonResponse.setStatus(StatusCode.FAIL);
        dataJsonResponse.setMessage(getMessage("fail"));
        if (result) {
            dataJsonResponse.setStatus(StatusCode.SC_OK);
            dataJsonResponse.setMessage(getMessage("success"));
        }
        String dataJson = dataJsonResponse.toJson(isNullToEmpty);
        return ResponseUtils.ok(dataJson);
    }

    /**
     * 根据参数返回结果集
     *
     * @param result：是否成功
     * @param data:返回数据
     * @param isNullToEmpty 是否空字段值转换
     * @return
     */
    public Response getResponse(boolean result, Object data, boolean isNullToEmpty) {
        DataJsonResponse dataJsonResponse = new DataJsonResponse();
        dataJsonResponse.setStatus(StatusCode.FAIL);
        dataJsonResponse.setMessage(getMessage("fail"));
        if (result) {
            dataJsonResponse.setStatus(StatusCode.SC_OK);
            dataJsonResponse.setMessage(getMessage("success"));
        }
        if (null != data) {
            dataJsonResponse.setData(data);
        }
        String dataJson = dataJsonResponse.toJson(isNullToEmpty);
        return ResponseUtils.ok(dataJson);
    }

    public Response getResponse(boolean status, String message, Object data) {
        int statusInt = status ? StatusCode.SC_OK : StatusCode.FAIL;
        return getResponse(statusInt, message, data);
    }

    public Response getResponse(int status, String message, Object data) {
        return getResponse(status, message, data, false);
    }

    /**
     * 获取返回信息
     *
     * @param status        返回状态
     * @param message       返回消息
     * @param data          接收数据
     * @param isNullToEmpty 是否空字段值转换
     * @return
     */
    public Response getResponse(int status, String message, Object data, boolean isNullToEmpty) {
        DataJsonResponse dataJsonResponse = new DataJsonResponse();
        if (data != null) {
            dataJsonResponse.setData(data);
        }
        dataJsonResponse.setStatus(status);
        dataJsonResponse.setMessage(message);
        String value = dataJsonResponse.toJson(isNullToEmpty);
        return ResponseUtils.ok(value);
    }

    /**
     * 获取ip地址
     *
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
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
}
