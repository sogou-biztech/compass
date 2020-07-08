package com.sogou.bizdev.compass.aggregation.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 提供数值相关的基本操作，主要用于聚合函数计算
 *
 * @author yk
 * @version 1.0.0
 * @since 1.0.0
 */
public class NumberUtil {

    public static Number add(Number n1, Number n2) {
        if (n1 == null && n2 == null) {
            return null;
        }
        if (n1 == null) {
            return n2;
        }
        if (n2 == null) {
            return n1;
        }
        if (!n1.getClass().equals(n2.getClass())) {
            throw new IllegalArgumentException();
        }

        if (isDecimal(n1)) {
            BigDecimal bdn1 = convert(BigDecimal.class, n1);
            BigDecimal bdn2 = convert(BigDecimal.class, n2);
            return convert(n1.getClass(), bdn1.add(bdn2));
        } else {
            BigInteger bin1 = convert(BigInteger.class, n1);
            BigInteger bin2 = convert(BigInteger.class, n2);
            return convert(n1.getClass(), bin1.add(bin2));
        }
    }

    private static boolean isDecimal(Number number) {
        if (number instanceof Float
            || number instanceof Double
            || number instanceof BigDecimal) {
            return true;
        }
        if (number instanceof Byte
            || number instanceof Short
            || number instanceof Integer
            || number instanceof Long
            || number instanceof BigInteger) {
            return false;
        }

        throw new IllegalArgumentException("unsupported number type=" + number.getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T convert(Class<T> targetType, Number n) {
        if (targetType == null) {
            throw new IllegalArgumentException();
        }
        if (n == null) {
            return null;
        }
        if (targetType.equals(Byte.class)) {
            return (T) Byte.valueOf(n.byteValue());
        }
        if (targetType.equals(Short.class)) {
            return (T) Short.valueOf(n.shortValue());
        }
        if (targetType.equals(Integer.class)) {
            return (T) Integer.valueOf(n.intValue());
        }
        if (targetType.equals(Long.class)) {
            return (T) Long.valueOf(n.longValue());
        }
        if (targetType.equals(Double.class)) {
            return (T) Double.valueOf(n.doubleValue());
        }
        if (targetType.equals(Float.class)) {
            return (T) Float.valueOf(n.floatValue());
        }
        if (targetType.equals(BigDecimal.class)) {
            if (n instanceof BigDecimal) {
                return (T) n;
            } else {
                return (T) BigDecimal.valueOf(n.floatValue());
            }
        }
        if (targetType.equals(BigInteger.class)) {
            if (n instanceof BigInteger) {
                return (T) n;
            } else {
                return (T) BigInteger.valueOf(n.longValue());
            }
        }

        throw new IllegalArgumentException();
    }

}