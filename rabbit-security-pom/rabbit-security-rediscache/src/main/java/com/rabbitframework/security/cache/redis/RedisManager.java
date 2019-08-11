package com.rabbitframework.security.cache.redis;

import java.util.Set;

public interface RedisManager {

	public byte[] get(byte[] key);

	public byte[] set(byte[] key, byte[] value);

	public byte[] set(byte[] key, byte[] value, int expire);

	public void del(byte[] key);

	public void flushDB();

	public Long dbSize();

	public Set<byte[]> keys(String pattern);
}
