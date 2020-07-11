package com.sogou.bizdev.compass.aggregation.util;

/**
 * 用于处理 in (routeEvicenceList)形式的查询的工具类
 *
 * @author yanke
 */
public class RangedSqlUtil {

    /**
     * 以pattern为分隔，将string切分成两部分
     *
     * @param string
     * @param pattern
     * @return
     */
    public static String[] split(String string, String pattern) {
        int idx = string.indexOf(pattern);

        if (idx < 0) {
            throw new IllegalArgumentException();
        }

        return new String[]{
            string.substring(0, idx),
            string.substring(idx + pattern.length())
        };
    }

    /**
     * 判断string中c字符出现的次数
     *
     * @param string
     * @param c
     * @return
     */
    public static int count(String string, char c) {
        int count = 0;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == c) {
                count++;
            }
        }

        return count;
    }

    /**
     * 构建?, ?, ?形式的字符串
     *
     * @param count 问号的个数
     * @return
     */
    public static String buildPlaceHolder(int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count - 1; i++) {
            builder.append("?, ");
        }
        builder.append("?");

        return builder.toString();
    }

}
