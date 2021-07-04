package com.rabbitframework.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rabbitframework.core.exceptions.DataParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    /**
     * 对象转json字符串
     *
     * @param obj
     * @param isNullToEmpty        是否null转空
     * @param isSkipTransientField 是否跳过@Transient字段
     * @return
     */
    public static String toJson(Object obj, boolean isNullToEmpty, boolean isSkipTransientField) {
        List<SerializerFeature> serializerFeatures = new ArrayList<>();
        if (isNullToEmpty) {
            serializerFeatures.add(SerializerFeature.WriteMapNullValue);
            serializerFeatures.add(SerializerFeature.WriteNullNumberAsZero);
            serializerFeatures.add(SerializerFeature.WriteNullListAsEmpty);
            serializerFeatures.add(SerializerFeature.WriteNullStringAsEmpty);
            serializerFeatures.add(SerializerFeature.WriteNullBooleanAsFalse);
        }
        if (isSkipTransientField) {
            serializerFeatures.add(SerializerFeature.SkipTransientField);
        }
        int size = serializerFeatures.size();
        if (size > 0) {
            SerializerFeature[] sf = new SerializerFeature[size];
            return toJson(obj, serializerFeatures.toArray(sf));
        }
        return toJson(obj);
    }

    /**
     * 对象转json字符串,不跳过{@link java.beans.Transient}
     *
     * @param obj
     * @param isNullToEmpty 是否null转空
     * @return
     */
    public static String toJson(Object obj, boolean isNullToEmpty) {
        return toJson(obj, isNullToEmpty, false);
    }

    /**
     * 对象转json字符串,跳过{@link java.beans.Transient}
     *
     * @param obj
     * @param isNullToEmpty
     * @return
     */
    public static String toJsonSkipTransient(Object obj, boolean isNullToEmpty) {
        return toJson(obj, isNullToEmpty, true);
    }

    /**
     * 对象转json字符串,封装{@link JSON}toJSONString方法
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static String toJson(Object obj, SerializerFeature... features) {
        return JSON.toJSONString(obj, features);
    }

    public static String toJson(Object obj, SerializeFilter filter, SerializerFeature... features) {
        return JSON.toJSONString(obj, filter, features);
    }

    public static <T> T getObject(String jsonString, Class<T> cls) {
        try {
            return JSON.parseObject(jsonString, cls);
        } catch (Exception e) {
            throw new DataParseException(e);
        }
    }

    public static <T> List<T> getListObject(String jsonString, Class<T> cls) {
        try {
            List<T> resultData = JSON.parseArray(jsonString, cls);
            if (resultData == null) {
                resultData = new ArrayList<T>();
            }
            return resultData;
        } catch (Exception e) {
            throw new DataParseException(e);
        }
    }

    public static List<Map<String, String>> getKeyStringMap(String jsonString) {
        List<Map<String, String>> list;
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, String>>>() {
            });
        } catch (Exception e) {
            throw new DataParseException(e);
        }
        if (list == null) {
            list = new ArrayList<Map<String, String>>();
        }
        return list;
    }

    public static List<Map<Long, String>> getKeyLongMap(String jsonString) {
        List<Map<Long, String>> list;
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<Long, String>>>() {
            });
        } catch (Exception e) {
            throw new DataParseException(e);
        }
        if (list == null) {
            list = new ArrayList<Map<Long, String>>();
        }
        return list;
    }
}
