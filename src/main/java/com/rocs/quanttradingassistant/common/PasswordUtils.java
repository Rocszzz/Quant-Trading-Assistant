package com.rocs.quanttradingassistant.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码摘要工具类
 *
 * @author Rocs
 * @since 2026/05/28
 */
public final class PasswordUtils {

    private static final String SHA_256 = "SHA-256";

    private PasswordUtils() {
    }

    /**
     * 使用 SHA-256 生成密码摘要
     *
     * @param rawPassword 原始密码
     * @return 十六进制摘要
     */
    public static String encode(String rawPassword) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
            byte[] digest = messageDigest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(digest.length * 2);
            for (byte item : digest) {
                builder.append(String.format("%02x", item));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable", exception);
        }
    }

    /**
     * 校验密码是否匹配
     *
     * @param rawPassword 原始密码
     * @param storedPassword 数据库存储的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String storedPassword) {
        if (storedPassword == null) {
            return false;
        }
        return storedPassword.equals(encode(rawPassword)) || storedPassword.equals(rawPassword);
    }
}
