package com.example.common_resource.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * Utility of MD5
 */
public class MD5Utils {

    private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);

    public static String hash(String content) {
        String hashString = null;
        try {
            byte[] hashBytes = Objects.requireNonNull(toHexString(md5(content))).getBytes(StandardCharsets.UTF_8);
            hashString = new String(hashBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Something wrong happened during the md5 hashing......");
        }
        return hashString;
    }

    private static byte[] md5(String content) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(content.getBytes(StandardCharsets.UTF_8));
            logger.info("success!");
            return messageDigest.digest();
        } catch (Exception e) {
            logger.error("The error of MD5 happened...", e);
        }
        return null;
    }

    private static String toHexString(byte[] hash) {
        if (hash == null) return null;
        StringBuilder buffer = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xff) < 0x10) {
                buffer.append("0");
            }
            buffer.append(Integer.toHexString(b & 0xff));
        }
        return buffer.toString();
    }
}
