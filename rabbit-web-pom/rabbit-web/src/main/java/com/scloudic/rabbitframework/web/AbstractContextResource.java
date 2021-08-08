package com.scloudic.rabbitframework.web;

import com.scloudic.rabbitframework.core.utils.StatusCode;
import com.scloudic.rabbitframework.web.resources.RabbitContextResource;
import com.scloudic.rabbitframework.web.utils.ResponseUtils;
import com.scloudic.rabbitframework.web.utils.ServletContextHelper;
import com.scloudic.rabbitframework.core.utils.StringUtils;

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
        return getSimpleResponse(result, data, true, true);
    }


    public Response getSimpleResponse(boolean result, Object data, boolean isNullToEmpty) {
        return getSimpleResponse(result, data, isNullToEmpty, true);
    }

    public Response getSimpleResponse(boolean result, Object data,
                                      boolean isNullToEmpty, boolean isSkipTransientField) {
        DataJsonResponse dataJsonResponse = new DataJsonResponse();
        if (data != null) {
            dataJsonResponse.setData(data);
        }
        dataJsonResponse.setStatus(StatusCode.FAIL.getValue());
        dataJsonResponse.setMessage(getMessage("fail"));
        if (result) {
            dataJsonResponse.setStatus(StatusCode.SC_OK.getValue());
            dataJsonResponse.setMessage(getMessage("success"));
        }
        String dataJson = dataJsonResponse.toJson(isNullToEmpty, isSkipTransientField);
        return ResponseUtils.ok(dataJson);
    }

    public Response getResponse(int status, String message) {
        return getResponse(status, message, null, true);
    }

    public Response getResponse(boolean status, String message, Object data) {
        int statusInt = status ? StatusCode.SC_OK.getValue() : StatusCode.FAIL.getValue();
        return getResponse(statusInt, message, data, true);
    }

    public Response getResponse(int status, String message, Object data, boolean isNullToEmpty) {
        return getResponse(status, message, data, isNullToEmpty, true);
    }

    public Response getResponse(int status, String message, Object data,
                                boolean isNullToEmpty,
                                boolean isSkipTransientField) {
        if (StringUtils.isBlank(message)) {
            message = getMessage("success");
            if (status != StatusCode.SC_OK.getValue()) {
                message = getMessage("fail");
            }
        }
        DataJsonResponse dataJsonResponse = new DataJsonResponse();
        if (data != null) {
            dataJsonResponse.setData(data);
        }
        dataJsonResponse.setStatus(status);
        dataJsonResponse.setMessage(message);
        String value = dataJsonResponse.toJson(isNullToEmpty, isSkipTransientField);
        return ResponseUtils.ok(value);
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
