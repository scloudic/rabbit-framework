package com.scloudic.rabbitframework.redisson.test;

import com.scloudic.rabbitframework.core.reflect.MetaObject;
import com.scloudic.rabbitframework.core.reflect.MetaObjectUtils;

import java.io.IOException;

public class RedissonMain {
	public static void main(String[] args) throws IOException {
		Model model = new Model();
		model.setId(1111);
		model.setName("222222");
		MetaObject metaObject = MetaObjectUtils.forObject(model);
		System.out.printf(metaObject.getValue("id").toString());
		// Config config = new Config();
		// config.useSingleServer().
		// setAddress("redis://47.92.170.84:6798")
		// .setPassword("medkazochensuredis.705");
		// RedissonClient redisson =
		// Redisson.create(Config.fromYAML(ResourceUtils.getResourceAsReader("redisson.yml")));
		// RBucket<String> bucket =
		// redisson.getBucket("sms:phone:customer:18573486618");
		// String a = bucket.get();
		// System.out.println("a:" + a);
		// RBinaryStream binaryStream = redisson.getBinaryStream("test");
		// binaryStream.set(SerializeUtils.serialize("aaa"));
		// System.out.println(SerializeUtils.deserialize(binaryStream.get()));
		// RLock rLock = redisson.getLock("aaa");
		// try {
		// boolean b = rLock.tryLock(10, TimeUnit.SECONDS);
		// System.out.println(b);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } finally {
		// rLock.unlock();
		// }
		// redisson.shutdown();
	}
}