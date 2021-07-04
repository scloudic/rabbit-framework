package com.rabbitframework.redisson;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;

public class RedisCache {
	private RedissonClient redissonClient;
	private final long LOCK_TIME = 10L;

	public void setRedissonClient(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	public <T> void list(String key, T value, long seconds) {
		try {
			RList<T> rList = redissonClient.getList(key);
			rList.add(value);
			if (seconds > 0) {
				rList.expire(seconds, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	public <T> RList<T> getList(String key) {
		try {
			RList<T> rList = redissonClient.getList(key);
			return rList;
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	public <T> long getListSize(String key) {
		try {
			RList<T> rList = redissonClient.getList(key);
			return rList.size();
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	/**
	 * 移除并返回列表 key 的头元素,对应redis命令为：lpop
	 * 
	 * @param key
	 * @return
	 */
	public <T> T poll(String key) {
		try {
			RQueue<T> queue = redissonClient.getQueue(key);
			return queue.poll();
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	public void set(String key, String value) {
		try {
			RBucket<String> bucket = redissonClient.getBucket(key);
			bucket.set(value);
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	public String get(String key) {
		try {
			RBucket<String> bucket = redissonClient.getBucket(key);
			return bucket.get();
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	public void set(String key, String value, long expire) {
		try {
			RBucket<String> bucket = redissonClient.getBucket(key);
			bucket.set(value, expire, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	public boolean del(String key) {
		try {
			return redissonClient.getBucket(key).delete();
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	public Iterator<String> keys(String key) {
		try {
			RKeys rkeys = redissonClient.getKeys();
			if (null != key && !"".equals(key)) {
				return rkeys.getKeysByPattern(key).iterator();
			}
			return rkeys.getKeys().iterator();
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	/**
	 * 阻塞加锁
	 * 
	 * @param key
	 */
	public void lock(String key) {
		RLock rLock = redissonClient.getLock(key);
		rLock.lock();
	}

	/**
	 * redis加锁，默认10秒
	 * 
	 * @param key
	 * @return
	 */
	public boolean tryLock(String key) {
		return tryLock(key, LOCK_TIME);
	}

	/**
	 * redis加锁,单位：秒
	 * 
	 * @param key
	 * @param time
	 * @return
	 */
	public boolean tryLock(String key, long time) {
		try {
			RLock rLock = redissonClient.getLock(key);
			return rLock.tryLock(time, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	public void unLock(String key) {
		try {
			RLock rLock = redissonClient.getLock(key);
			rLock.unlock();
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	public long incr(String key) {
		try {
			return redissonClient.getAtomicLong(key).incrementAndGet();
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}
	}

	public long decr(String key) {
		try {
			RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
			if (atomicLong == null) {
				return 0;
			}
			return atomicLong.decrementAndGet();
		} catch (Exception e) {
			throw new RedisException(e.getMessage(), e);
		}

	}
}
