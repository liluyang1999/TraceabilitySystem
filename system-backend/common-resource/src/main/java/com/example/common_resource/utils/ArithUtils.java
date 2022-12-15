package com.example.common_resource.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility of Arithmetic
 */
public class ArithUtils {

    private static final int DIVIDE_SCALE = 10;

    private ArithUtils() { }

    public static double round(double value, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(value));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static double add(double value1, double value2) {
        BigDecimal decimal1 = new BigDecimal(Double.toString(value1));
        BigDecimal decimal2 = new BigDecimal(Double.toString(value2));
        return decimal1.add(decimal2).doubleValue();
    }

    public static double subtract(double value1, double value2) {
        BigDecimal decimal1 = new BigDecimal(Double.toString(value1));
        BigDecimal decimal2 = new BigDecimal(Double.toString(value2));
        return decimal1.subtract(decimal2).doubleValue();
    }

    public static double multiply(double value1, double value2) {
        BigDecimal decimal1 = new BigDecimal(Double.toString(value1));
        BigDecimal decimal2 = new BigDecimal(Double.toString(value2));
        return decimal1.multiply(decimal2).doubleValue();
    }

    public static double divide(double value1, double value2) {
        return divide(value1, value2, DIVIDE_SCALE);
    }

    public static double divide(double value1, double value2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal decimal1 = new BigDecimal(Double.toString(value1));
        BigDecimal decimal2 = new BigDecimal(Double.toString(value2));
        if (decimal1.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.doubleValue();
        }
        return decimal1.divide(decimal2, scale, RoundingMode.HALF_UP).doubleValue();
    }
}
