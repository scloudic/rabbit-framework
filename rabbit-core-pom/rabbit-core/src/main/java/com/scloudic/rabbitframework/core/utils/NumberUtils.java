package com.scloudic.rabbitframework.core.utils;

public class NumberUtils {

	/**
	 * 如果值为空返回0
	 * 
	 * @param value value
	 * @return 空返回0
	 */
	public static long getValueByLong(Long value) {
		return getValueByLong(value, 0L);
	}

	public static long getValueByLong(Long value, long defaultValue) {
		long result = defaultValue;
		if (value == null) {
			return result;
		}
		return value.longValue();
	}

	/**
	 * 如果为空返回0
	 * 
	 * @param value value
	 * @return 空返回0
	 */
	public static int getValueByInteger(Integer value) {
		return getValueByInteger(value, 0);
	}

	public static int getValueByInteger(Integer value, int defaultValue) {
		int result = defaultValue;
		if (value == null) {
			return result;
		}
		return value.intValue();
	}
}
