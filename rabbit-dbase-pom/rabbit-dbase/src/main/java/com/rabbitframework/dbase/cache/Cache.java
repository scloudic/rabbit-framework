package com.rabbitframework.dbase.cache;


/**
 * 缓存接口类
 */
public interface Cache {
    /**
     * 缓存id
     *
     * @return
     */
    String getId();

    /**
     * 缓存大小
     *
     * @return
     */
    int getSize();

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    void putObject(Object key, Object value);

    /**
     * 获取缓存中对象
     *
     * @param key
     * @return
     */
    Object getObject(Object key);

    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    Object removeObject(Object key);

    /**
     * 清除缓存
     */
    void clear();
}
