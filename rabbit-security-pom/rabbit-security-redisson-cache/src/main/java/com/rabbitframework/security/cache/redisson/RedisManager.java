package com.rabbitframework.security.cache.redisson;

import java.util.Set;

public interface RedisManager {

	public byte[] get(String key);

	public byte[] set(String key, byte[] value);

	public byte[] set(String key, byte[] value, int expire);

	public void del(String key);

	public void flushDB();

	public Long dbSize();

	public Set<String> keys(String pattern);
}
