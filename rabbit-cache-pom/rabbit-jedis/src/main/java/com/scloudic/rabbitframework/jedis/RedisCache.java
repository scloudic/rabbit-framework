package com.scloudic.rabbitframework.jedis;

import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * redis缓存管理
 *
 * @author: justin.liang
 * @date: 2017/1/22 17:47
 */
public interface RedisCache<T> {
	/**
	 * 返回有序集 key 中，指定区间内的成员
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max);

	/**
	 * 返回有序集 key 中, -inf 和 +inf方式实现
	 *
	 * @param key
	 * @return
	 */
	public Set<Tuple> zrangeByScoreWithScores(String key);

	/**
	 * 将字符串值 value 关联到 key 。 如果 key 已经持有其他值， SET 就覆写旧值，无视类型
	 *
	 * @param key
	 * @param value
	 */
	public void set(String key, String value);

	/**
	 * 返回 key 所关联的字符串值。
	 * <p>
	 * 如果 key 不存在那么返回特殊值 nil
	 * <p>
	 * 假如 key 储存的值不是字符串类型，返回一个错误，因为 GET 只能用于处理字符串值。
	 *
	 * @param key
	 * @return
	 */
	public String get(String key);

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 如果 key 不存在，一个新的哈希表被创建并进行HSET操作。 如果域 field
	 * 已经存在于哈希表中，旧值将被覆盖。
	 *
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hset(String key, String field, String value);

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 *
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field);

	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。s
	 *
	 * @param key
	 * @param field
	 */
	public Long hdel(String key, String... field);

	/**
	 * 将 key 中储存的数字值增一。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
	 *
	 * @param key
	 */
	public Long incr(String key);

	/**
	 * 删除给定的一个或多个 key 。 不存在的 key 会被忽略
	 *
	 * @param key
	 */
	public void del(String key);

	/**
	 * 将字符串值 value 关联到 key ,并设置过期时间。 如果 key 已经持有其他值， SET 就覆写旧值，无视类型
	 *
	 * @return void 返回类型
	 * @Title: set
	 */
	public void set(String key, String value, int expire);

	/**
	 * 当且仅当key-value 不存在时，将字符串值 value 关联到 key 如果 key 已经持有其他值，不做任何操作
	 *
	 * @return void 返回类型
	 * @Title: setnx
	 */
	public Long setnx(String key, String value);

	/**
	 * 当且仅当key-value 不存在时，将字符串值 value 关联到 key,并原子性地设置过期时间 如果 key 已经持有其他值，不做任何操作
	 *
	 * @return void 返回类型
	 * @Title: setnxex
	 */
	public String setnxex(String key, String value, int expire);

	/**
	 * 关闭缓存
	 * 
	 * @param t
	 */
	public void close(T t);

	/**
	 * 获取jedis
	 * 
	 * @return
	 */
	public T getJedis();
}
