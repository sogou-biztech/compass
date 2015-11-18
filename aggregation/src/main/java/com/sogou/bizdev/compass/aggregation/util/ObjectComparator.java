package com.sogou.bizdev.compass.aggregation.util;

import java.util.Comparator;

/**
 * Object比较器
 * 当两个被比较的对象均不支持Comparable时，抛出IllegalArgumentException
 *
 * @author yk
 * @version 1.0.0
 * @since 1.0.0
 */
public class ObjectComparator implements Comparator<Object> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (o1 instanceof Comparable) {
            return ((Comparable) o1).compareTo(o2);
        }
        if (o2 instanceof Comparable) {
            return -((Comparable) o2).compareTo(o1);
        }

        throw new IllegalArgumentException("neither o1 nor o2 supports the Comparable operation");
    }
    
}