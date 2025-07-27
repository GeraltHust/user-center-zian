package com.zian.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  MD5 加密/验证工具
 */
public class MD5Util {

    public static final String SALT = "salt";
    /**
     * MD5加密
     * @param input 要加密的字符串
     * @return 加密后的十六进制字符串
     */
    public static String md5(String input) {
        if (input == null) {
            return null;
        }

        try {
            // 获取MD5摘要算法的MessageDigest对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字节数组更新摘要
            md.update(input.getBytes());

            // 完成哈希计算，获得密文
            byte[] digest = md.digest();

            // 将字节数组转换为十六进制字符串
            return bytesToHex(digest);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    /**
     * 带盐值的MD5加密
     * @param input 要加密的字符串
     * @param salt 盐值
     * @return 加密后的十六进制字符串
     */
    public static String md5WithSalt(String input, String salt) {
        if (input == null || salt == null) {
            return null;
        }
        // 将密码和盐值拼接后进行MD5加密
        return md5(input + salt);
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            // 将每个字节转换为两位的十六进制数
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                sb.append('0');  // 补零
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 验证密码
     * @param input 原始密码
     * @param encrypted 加密后的密码
     * @return 是否匹配
     */
    public static boolean verify(String input, String encrypted) {
        if (input == null || encrypted == null) {
            return false;
        }
        return md5(input).equals(encrypted);
    }

    /**
     * 验证带盐值的密码
     * @param input 原始密码
     * @param salt 盐值
     * @param encrypted 加密后的密码
     * @return 是否匹配
     */
    public static boolean verifyWithSalt(String input, String salt, String encrypted) {
        if (input == null || salt == null || encrypted == null) {
            return false;
        }
        return md5WithSalt(input, salt).equals(encrypted);
    }

}