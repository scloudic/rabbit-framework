package com.rabbitframework.jbatis.cache.decorators;

import com.rabbitframework.jbatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggingCache implements Cache {
    private Logger logger;
    private Cache delegate;
    protected long requests = 0;
    protected long hits = 0;

    public LoggingCache(Cache delegate) {
        this.delegate = delegate;
        this.logger = LoggerFactory.getLogger(getId());
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public int getSize() {
        return delegate.getSize();
    }

    @Override
    public void putObject(Object key, Object value) {
        delegate.putObject(key, value);
    }

    @Override
    public Object getObject(Object key) {
        requests++;
        final Object value = delegate.getObject(key);
        if (value != null) {
            hits++;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Cache Hit Ratio [" + getId() + "]: " + getHitRatio());
        }
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        return delegate.removeObject(key);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    private double getHitRatio() {
        return (double) hits / (double) requests;
    }

//    @Override
//    public ReadWriteLock getReadWriteLock() {
//        return null;
//    }

}
