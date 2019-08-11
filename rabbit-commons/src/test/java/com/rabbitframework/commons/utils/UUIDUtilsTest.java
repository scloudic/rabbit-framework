package com.rabbitframework.commons.utils;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.UUIDTimer;

public class UUIDUtilsTest {
	public static void main(String[] args) throws IOException {
		// 3c:97:0e:72:a4:49
		// 3c:97:0e:72:a4:49
//		System.out.println(EthernetAddress.fromInterface());
//
//		UUIDTimer _sharedTimer = new UUIDTimer(new java.util.Random(System.currentTimeMillis()), null);
//		System.out.println(_sharedTimer.getClockSequence());
		
		System.out.println(getOrderIdByUUId());
	}

	public static String getOrderIdByUUId() {
		int machineId = 1;// 最大支持1-9个集群机器部署
//		17e44d9bf28c11e696513c970e72a449
//		c08d7d1cf28c11e6b6fc3c970e72a449
		String uuid = UUIDUtils.getRandomUUID().toString();
		System.out.println(uuid);
		int hashCodeV = uuid.hashCode();
		System.out.println(hashCodeV);
		if (hashCodeV < 0) {// 有可能是负数
			hashCodeV = -hashCodeV;
		}
		// 0 代表前面补充0
		// 4 代表长度为4
		// d 代表参数为正数型
		return String.format("%d", hashCodeV);
	}
}
