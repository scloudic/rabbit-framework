package com.scloudic.rabbitframework.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scloudic.rabbitframework.core.exceptions.DataParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    /**
     * 对象转json字符串
     *
     * @param obj                  obj
     * @param isNullToEmpty        是否null转空
     * @param isSkipTransientField 是否跳过@Transient字段
     * @return string
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
     * @param obj           obj
     * @param isNullToEmpty 是否null转空
     * @return string
     */
    public static String toJson(Object obj, boolean isNullToEmpty) {
        return toJson(obj, isNullToEmpty, false);
    }

    /**
     * 对象转json字符串,跳过{@link java.beans.Transient}
     *
     * @param obj           obj
     * @param isNullToEmpty 空转""
     * @return string
     */
    public static String toJsonSkipTransient(Object obj, boolean isNullToEmpty) {
        return toJson(obj, isNullToEmpty, true);
    }

    /**
     * 对象转json字符串,封装{@link JSON}toJSONString方法
     *
     * @param obj obj
     * @return string
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
            return JSON.parseObject(jsonString, cls, Feature.OrderedField);
        } catch (Exception e) {
            throw new DataParseException(e);
        }
    }

    public static <K, T> Map<K, T> getMap(String jsonString) {
        try {
            return JSON.parseObject(jsonString, new TypeReference<Map<K, T>>() {
            });
        } catch (Exception e) {
            throw new DataParseException(e);
        }
    }

    public static <T> List<T> getList(String jsonString, Class<T> cls) {
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

    public static <K, T> List<Map<K, T>> getListMap(String jsonString) {
        List<Map<K, T>> list;
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<K, T>>>() {
            }, Feature.OrderedField);
        } catch (Exception e) {
            throw new DataParseException(e);
        }
        if (list == null) {
            list = new ArrayList<Map<K, T>>();
        }
        return list;
    }
}
