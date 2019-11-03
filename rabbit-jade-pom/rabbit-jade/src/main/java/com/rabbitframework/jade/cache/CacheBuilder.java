package com.rabbitframework.jade.cache;

import com.rabbitframework.jade.reflect.MetaObject;
import com.rabbitframework.jade.reflect.SystemMetaObject;
import com.rabbitframework.jade.cache.decorators.LoggingCache;
import com.rabbitframework.jade.cache.decorators.LruCache;
import com.rabbitframework.jade.cache.decorators.ScheduledCache;
import com.rabbitframework.jade.cache.decorators.SynchronizedCache;
import com.rabbitframework.jade.cache.impl.MapCache;
import com.rabbitframework.jade.exceptions.CacheException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class CacheBuilder {
    private String id;
    private Class<? extends Cache> implementation;
    private List<Class<? extends Cache>> decorators;
    private Long flushInterval;
    private Integer size;

    public CacheBuilder(String id) {
        this.id = id;
        decorators = new ArrayList<Class<? extends Cache>>();
    }

    public CacheBuilder implementation(Class<? extends Cache> implementation) {
        this.implementation = implementation;
        return this;
    }

    public CacheBuilder addDecorator(Class<? extends Cache> decorator) {
        if (decorator != null) {
            decorators.add(decorator);
        }
        return this;
    }

    public CacheBuilder size(int size) {
        this.size = size;
        return this;
    }

    public CacheBuilder clearInterval(Long clearInterval) {
        flushInterval = clearInterval;
        return this;
    }

    public Cache builder() {
        setDefaultImplementations();
        Cache cache = newBaseCacheInstance(implementation, id);
        if (MapCache.class.equals(cache.getClass())) {
            for (Class<? extends Cache> decorator : decorators) {
                cache = newCacheDecoratorInstance(decorator, cache);
            }
            cache = setStandardDecorators(cache);
        }
//        else if (!LoggingCache.class.isAssignableFrom(cache.getClass())) {
//            cache = new LoggingCache(cache);
//        }
        return cache;
    }

    private Cache setStandardDecorators(Cache cache) {
        try {
            MetaObject metaCache = SystemMetaObject.forObject(cache);
            if (size != null && metaCache.hasSetter("size")) {
                metaCache.setValue("size", size);
            }
            if (flushInterval != null) {
                cache = new ScheduledCache(cache);
                ((ScheduledCache) cache).setClearInterval(flushInterval);
            }

            cache = new LoggingCache(cache);
            cache = new SynchronizedCache(cache);
            return cache;
        } catch (Exception e) {
            throw new CacheException(
                    "Error building standard cache decorators.  Cause: " + e, e);
        }
    }

    private void setDefaultImplementations() {
        if (implementation == null) {
            implementation = MapCache.class;
            if (decorators.size() == 0) {
                decorators.add(LruCache.class);
            }
        }
    }

    private Cache newBaseCacheInstance(Class<? extends Cache> cacheClass, String id) {
        Constructor<? extends Cache> cacheConstructor = getBaseCacheConstructor(cacheClass);
        try {
            return cacheConstructor.newInstance(id);
        } catch (Exception e) {
            throw new CacheException("Could not instantiate cache implementation (" + cacheClass + "). Cause: " + e, e);
        }
    }

    private Constructor<? extends Cache> getBaseCacheConstructor(Class<? extends Cache> cacheClass) {
        try {
            return cacheClass.getConstructor(String.class);
        } catch (Exception e) {
            throw new CacheException("Invalid base cache implementation (" + cacheClass + ").  " +
                    "Base cache implementations must have a constructor that takes a String id as a parameter.  Cause: " + e, e);
        }
    }

    private Cache newCacheDecoratorInstance(Class<? extends Cache> cacheClass, Cache base) {
        Constructor<? extends Cache> cacheConstructor = getCacheDecoratorConstructor(cacheClass);
        try {
            return cacheConstructor.newInstance(base);
        } catch (Exception e) {
            throw new CacheException("Could not instantiate cache decorator (" + cacheClass + "). Cause: " + e, e);
        }
    }

    private Constructor<? extends Cache> getCacheDecoratorConstructor(Class<? extends Cache> cacheClass) {
        try {
            return cacheClass.getConstructor(Cache.class);
        } catch (Exception e) {
            throw new CacheException("Invalid cache decorator (" + cacheClass + ").  " +
                    "Cache decorators must have a constructor that takes a Cache instance as a parameter.  Cause: " + e, e);
        }
    }

}
