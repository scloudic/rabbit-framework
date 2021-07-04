package com.rabbitframework.jbatis.cache.decorators;

import com.rabbitframework.jbatis.cache.Cache;

public class ScheduledCache implements Cache {

	private Cache delegate;
	protected long clearInterval;
	protected long lastClear;

	public ScheduledCache(Cache delegate) {
		this.delegate = delegate;
		this.clearInterval = 60 * 60 * 1000; // 1 hour
		this.lastClear = System.currentTimeMillis();
	}

	public void setClearInterval(long clearInterval) {
		this.clearInterval = clearInterval;
	}

	@Override
	public String getId() {
		return delegate.getId();
	}

	@Override
	public int getSize() {
		clearWhenStale();
		return delegate.getSize();
	}

	@Override
	public void putObject(Object key, Object object) {
		clearWhenStale();
		delegate.putObject(key, object);
	}

	@Override
	public Object getObject(Object key) {
		if (clearWhenStale()) {
			return null;
		} else {
			return delegate.getObject(key);
		}
	}

	@Override
	public Object removeObject(Object key) {
		clearWhenStale();
		return delegate.removeObject(key);
	}

	@Override
	public void clear() {
		lastClear = System.currentTimeMillis();
		delegate.clear();
	}

//	@Override
//	public ReadWriteLock getReadWriteLock() {
//		return null;
//	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	private boolean clearWhenStale() {
		if (System.currentTimeMillis() - lastClear > clearInterval) {
			clear();
			return true;
		}
		return false;
	}
}
