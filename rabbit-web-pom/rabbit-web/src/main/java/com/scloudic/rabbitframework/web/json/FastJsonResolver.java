package com.scloudic.rabbitframework.web.json;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;

import javax.ws.rs.ext.ContextResolver;

public class FastJsonResolver implements ContextResolver<FastJsonConfig> {
    // 用FastJson替换掉默认的Jackson
    public FastJsonConfig getContext(Class<?> type) {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.SkipTransientField);
        return fastJsonConfig;
    }
}
