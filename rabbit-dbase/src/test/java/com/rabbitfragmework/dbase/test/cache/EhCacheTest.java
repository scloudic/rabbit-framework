package com.rabbitfragmework.dbase.test.cache;

import org.junit.Test;

import com.rabbitframework.dbase.cache.impl.EhcacheCache;

public class EhCacheTest {
	@Test
	public void testEhCache() {
		EhcacheCache ehcacheCache = new EhcacheCache("submitProcessInst");
		// ehcacheCache.putObject("test", "firstCache");
		// ehcacheCache.putObject("test2", "firstCache2");
		Object obj = ehcacheCache.getObject("test");
		// Object obj = ehcacheCache.removeObject("test2");
		System.out.println("obj:" + obj);
	}
}
