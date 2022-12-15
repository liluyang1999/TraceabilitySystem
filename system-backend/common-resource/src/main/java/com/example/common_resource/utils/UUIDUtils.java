package com.example.common_resource.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * UUID Generator
 */
public class UUIDUtils {

    // Simplify the format of uuid string
    public static String simpleUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // Achieve a random UUID with the ThreadLocalRandom for better efficiency
    public static String fastUUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f; /* clear version */
        randomBytes[6] |= 0x40; /* set to version 4 */
        randomBytes[8] &= 0x3f; /* clear variant */
        randomBytes[8] |= 0x80; /* set to IETF variant */

        long mostSigBits = 0;
        long leastSigBits = 0;
        for (int i = 0; i < 8; i++) {
            mostSigBits = (mostSigBits << 8) | (randomBytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            leastSigBits = (leastSigBits << 8) | (randomBytes[i] & 0xff);
        }
        return (new UUID(mostSigBits, leastSigBits)).toString().replaceAll("-", "");
    }
}
