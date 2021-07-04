package com.rabbitframework.redisson.test;

import java.util.Iterator;

import com.rabbitframework.redisson.test.core.AbstractSpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rabbitframework.redisson.RedisCache;

public class RedissonSpringTest extends AbstractSpringTestCase {
	@Autowired
	private RedisCache redisCache;

	@Test
	public void testSet() {
		redisCache.set("test", "1111");
	}

	@Test
	public void testGet() {
		System.out.println(redisCache.get("test"));
	}

	@Test
	public void testDel() {
		System.out.println(redisCache.del("aaa"));
	}

	@Test
	public void testlist() {
		long value = redisCache.getListSize("list");
		System.out.println(value);
	}

	@Test
	public void keys() {
		Iterator<String> it = redisCache.keys("3232232*");
		System.out.println(it.hasNext());
	}
}