package com.rabbitframework.core.utils;

/**
 * @author justin.liang
 */
public class DigestUtils extends org.apache.commons.codec.digest.DigestUtils {
    public static String md5ToStr(String data) {
        return md5Hex(data.getBytes());
    }
}
