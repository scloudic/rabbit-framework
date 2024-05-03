package com.scloudic.rabbitframework.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scloudic.rabbitframework.core.exceptions.DataParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    /**
     * 对象转json字符串
     *
     * @param obj obj
     * @return string
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        String resultValue = null;
        try {
            resultValue = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("json转换异常:" + e.getMessage(), e);
            new DataParseException(e);
        }
        return resultValue;
    }

    public static <T> T getObject(String jsonString, Class<T> cls) {
        try {
            if (StringUtils.isBlank(jsonString)) {
                return null;
            }
            return objectMapper.readValue(jsonString, cls);
        } catch (Exception e) {
            throw new DataParseException(e);
        }
    }


    public static <T> T getObject(String jsonString, TypeReference<T> cls) {
        try {
            if (StringUtils.isBlank(jsonString)) {
                return null;
            }
            return objectMapper.readValue(jsonString, cls);
        } catch (Exception e) {
            throw new DataParseException(e);
        }
    }

    public static <K, T> Map<K, T> getMap(String jsonString) {
        try {
            return getObject(jsonString, new TypeReference<Map<K, T>>() {
            });
        } catch (Exception e) {
            throw new DataParseException(e);
        }
    }

    public static <T> List<T> getList(String jsonString, Class<T> cls) {
        if (StringUtils.isBlank(jsonString)) {
            return new ArrayList<T>();
        }
        try {
            return objectMapper.readValue(jsonString,
                    objectMapper.getTypeFactory().constructParametricType(List.class, cls));
        } catch (Exception e) {
            throw new DataParseException(e);
        }
    }

    public static <K, T> List<Map<K, T>> getListMap(String jsonString) {
        List<Map<K, T>> list;
        if (StringUtils.isBlank(jsonString)) {
            return new ArrayList<Map<K, T>>();
        }
        try {
            list = getObject(jsonString, new TypeReference<List<Map<K, T>>>() {
            });
        } catch (Exception e) {
            throw new DataParseException(e);
        }
        if (list == null) {
            list = new ArrayList<Map<K, T>>();
        }
        return list;
    }
}
