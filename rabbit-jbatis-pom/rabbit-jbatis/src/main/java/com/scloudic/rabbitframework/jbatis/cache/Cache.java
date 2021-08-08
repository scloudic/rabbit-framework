package com.scloudic.rabbitframework.jbatis.cache;


/**
 * 缓存接口类
 */
public interface Cache {

    String getId();


    int getSize();


    void putObject(Object key, Object value);

    Object getObject(Object key);


    Object removeObject(Object key);

    
    void clear();
}
