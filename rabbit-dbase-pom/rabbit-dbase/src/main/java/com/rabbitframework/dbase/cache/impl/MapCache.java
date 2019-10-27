package com.rabbitframework.dbase.cache.impl;

import java.util.HashMap;
import java.util.Map;

import com.rabbitframework.dbase.cache.Cache;
import com.rabbitframework.dbase.exceptions.CacheException;

public class MapCache implements Cache {
    private String id;
    private Map<Object, Object> cache = new HashMap<Object, Object>();

    public MapCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getSize() {
        return cache.size();
    }

    @Override
    public void putObject(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return cache.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

//    @Override
//    public ReadWriteLock getReadWriteLock() {
//        return null;
//    }

    @Override
    public boolean equals(Object obj) {
        if (getId() == null) {
            throw new CacheException("cache instances require an ID.");
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cache)) {
            return false;
        }

        Cache otherCache = (Cache) obj;
        return getId().equals(otherCache.getId());
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            throw new CacheException("cache instances require an ID");
        }
        return getId().hashCode();
    }
}
