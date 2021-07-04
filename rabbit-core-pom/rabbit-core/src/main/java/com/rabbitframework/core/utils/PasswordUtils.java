package com.rabbitframework.core.utils;

import java.util.Random;

/**
 * 随机加盐密码生成工具类
 */
public class PasswordUtils {
	private static final String key = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	/**
	 * 生成含有随机盐的密码
	 */
	public static String generate(String password) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(16);
		int keyLength = key.length();
		for (int i = 0; i < 16; i++) {
			int number = random.nextInt(keyLength);
			sb.append(key.charAt(number));
		}
		String salt = sb.toString();
		password = DigestUtils.md5Hex(password + salt);
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = password.charAt(i / 3 * 2);
			char c = salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = password.charAt(i / 3 * 2 + 1);
		}
		return new String(cs);
	}

	/**
	 * 校验密码是否正确
	 */
	public static boolean verify(String password, String md5) {
		char[] cs1 = new char[32];
		char[] cs2 = new char[16];
		for (int i = 0; i < 48; i += 3) {
			cs1[i / 3 * 2] = md5.charAt(i);
			cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
			cs2[i / 3] = md5.charAt(i + 1);
		}
		String salt = new String(cs2);
		return DigestUtils.md5Hex(password + salt).equals(String.valueOf(cs1));
	}

	public static void main(String[] args) {
		// 加密+加盐
		String password1 = generate("admin");
		System.out.println("结果：" + password1 + "   长度：" + password1.length());
		// 解码
		System.out.println(verify("admin", password1));
	}
}
