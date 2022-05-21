package com.scloudic.rabbitframework.core.uid;

public interface UIdGenerator {
	public String nextId(GeneratorType generatorType);

	public static enum GeneratorType {
		// 标记雪花算法
		SNOWFLAKE,
		// uuid的hashcode
		UUIDHASHCODE,
		// 时间戳
		TIMESTAMP;
	}
}
