package com.scloudic.rabbitframework.core.utils;


import com.scloudic.rabbitframework.core.exceptions.CodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private final static Logger log = LoggerFactory.getLogger(AESUtils.class);
    //编码方式
    private static final String CODE_TYPE = "UTF-8";
    //填充类型
    private static final String AES_TYPE = "AES/ECB/PKCS5Padding";

    /**
     * aes加密,返回base64编码后的字符串
     *
     * @param secretKey
     * @param content
     * @return
     */
    public static String encrypt(String secretKey, String content) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedData = cipher.doFinal(content.getBytes(CODE_TYPE));
            return Base64Utils.encodeToString(encryptedData);
        } catch (Exception e) {
            log.error(String.format("加密失败, key = %s , data = %s " + e, secretKey, content));
            throw new CodecException(e.getMessage(), e);
        }
    }

    /**
     * 将base64位内容进行解密
     *
     * @param secretKey
     * @param base64Encrypt
     * @return
     */
    public static String decrypt(String secretKey, String base64Encrypt) {
        try {
            byte[] byteContent = Base64Utils.decode(base64Encrypt);
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedData = cipher.doFinal(byteContent);
            return new String(decryptedData, CODE_TYPE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CodecException(e.getMessage(), e);
        }
    }
}