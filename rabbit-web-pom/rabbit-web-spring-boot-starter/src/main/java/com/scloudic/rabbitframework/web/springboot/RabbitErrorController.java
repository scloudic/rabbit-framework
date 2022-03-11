package com.scloudic.rabbitframework.web.springboot;

import com.scloudic.rabbitframework.core.utils.CommonResponseUrl;
import com.scloudic.rabbitframework.core.utils.JsonUtils;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Controller
public class RabbitErrorController extends BasicErrorController {
    private static final Logger logger = LoggerFactory.getLogger(RabbitErrorController.class);

    public RabbitErrorController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(), serverProperties.getError());
    }

    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> originalMsgMap = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML));
        logger.error("html请求发生错误,错误消息：" + JsonUtils.toJson(originalMsgMap));
        String url = CommonResponseUrl.getSys404ErrorUrl();
        if (StringUtils.isBlank(url)) {
            return super.errorHtml(request, response);
        }
        HttpStatus status = getStatus(request);
        response.setStatus(status.value());
        Map<String, Object> model = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML));
        return new ModelAndView(url, model);
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> originalMsgMap = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        logger.error("请求发生错误,错误消息：" + JsonUtils.toJson(originalMsgMap));
        String path = (String) originalMsgMap.get("path");
        String error = (String) originalMsgMap.get("error");
        StringJoiner joiner = new StringJoiner(",", "", "");
        joiner.add(path).add(error);
        map.put("status", status.value());
        map.put("message", joiner.toString());
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
}
