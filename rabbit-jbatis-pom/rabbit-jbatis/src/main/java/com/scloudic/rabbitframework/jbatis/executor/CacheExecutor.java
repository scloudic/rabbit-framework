package com.scloudic.rabbitframework.jbatis.executor;

import java.util.List;

import com.scloudic.rabbitframework.jbatis.cache.Cache;
import com.scloudic.rabbitframework.jbatis.mapping.MappedStatement;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;

public class CacheExecutor implements Executor {
    private Executor delegate;

    public CacheExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public int update(MappedStatement ms, Object parameter, String dynamicSQL) {
        int result = 0;
        result = delegate.update(ms, parameter, dynamicSQL);
        Cache cache = ms.getCache();
        if (cache != null) {
            String[] keys = ms.getCacheKey();
            for (String key : keys) {
                cache.removeObject(key);
            }
        }
        return result;
    }

    @Override
    public int batchUpdate(MappedStatement ms, List<Object> parameter, String dynamicSQL) {
        int result = 0;
        result = delegate.batchUpdate(ms, parameter, dynamicSQL);
        Cache cache = ms.getCache();
        if (cache != null) {
            String[] keys = ms.getCacheKey();
            for (String key : keys) {
                cache.removeObject(key);
            }
        }
        return result;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter,
                             RowBounds rowBounds, String dynamicSQL) {
        List<E> result = null;
        Cache cache = ms.getCache();
        String[] keys = ms.getCacheKey();
        if (cache != null) {
            if (keys != null && keys.length > 0) {
                result = (List<E>) cache.getObject(keys[0]);
            }
        }
        if (result == null) {
            result = delegate.query(ms, parameter, rowBounds, dynamicSQL);
            if (result != null && result.size() > 0 && cache != null) {
                if (keys != null && keys.length > 0) {
                    cache.putObject(keys[0], result);
                }
            }
        }
        return result;
    }
}
