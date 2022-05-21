package com.scloudic.rabbitframework.redisson.test;

import java.util.Iterator;

import com.scloudic.rabbitframework.redisson.test.core.AbstractSpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scloudic.rabbitframework.redisson.RedisCache;

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

    @Test
    public void incrWithExpire() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    long a = redisCache.incrementAndGetWithExpire("incrWithExpire", 0, 20);
                    System.out.println(Thread.currentThread().getName() + ",a=" + a);
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    long a = redisCache.incrementAndGetWithExpire("incrWithExpire", 0, 20);
                    System.out.println(Thread.currentThread().getName() + ",a=" + a);
                }
            }
        }).start();

        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(1000L);
            } catch (Exception e) {

            }

        }
    }

    @Test
    public void lock() {
        boolean isLock = redisCache.tryLock("test:lock", 10);
        System.out.println(isLock);
        redisCache.unLock("test:lock");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean isLock = true;
//                for (int i = 0; i < 100; i++) {
//                    long start = System.currentTimeMillis();
//                    isLock = redisCache.tryLock("test:lock", 10);
//                    System.out.println(System.currentTimeMillis() - start);
//                    System.out.println(isLock);
//                }
//            }
//        }).start();
    }

    @Test
    public void unlock() {
        redisCache.unLock("test:lock");

    }
}