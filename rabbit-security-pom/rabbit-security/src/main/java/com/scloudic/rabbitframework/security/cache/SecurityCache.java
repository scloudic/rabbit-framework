package com.scloudic.rabbitframework.security.cache;

import org.apache.shiro.cache.Cache;

public interface SecurityCache<K, V> extends Cache<K, V> {
    /**
     * 过期时间,单位：毫秒
     *
     * @return
     */
    public long getExpireTime();
}
